package com.ssk.bidwise.controller.agent;

import com.alibaba.cloud.ai.graph.NodeOutput;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.action.InterruptionMetadata;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
public class AgentController {

    private final ReactAgent reactAgent;

    private final Map<String, InterruptionMetadata> map = new ConcurrentHashMap<>();

    public AgentController(ReactAgent reactAgent) {

        this.reactAgent = reactAgent;
    }

    @GetMapping("/invoke")
    @ResponseBody
    public AgentInvokeResponse invoke(@RequestParam("query") String query,
                                      @RequestParam("threadId") String threadId
    ) throws Exception {

        try {
            RunnableConfig runnableConfig = RunnableConfig.builder().threadId(threadId).build();
            NodeOutput nodeOutput = reactAgent.invokeAndGetOutput(query, runnableConfig).orElseThrow();
            // 检查是否有中断需要人工干预
            List<InterruptionMetadata.ToolFeedback> toolFeedbacks = extractToolFeedbacks(nodeOutput);

            // 如果有中断，保存元数据供后续反馈使用
            if (!toolFeedbacks.isEmpty()) {
                // 这里需要根据实际情况获取 InterruptionMetadata
                // map.put(threadId, interruptionMetadata);
                log.info("Agent requires human intervention for threadId: {}, feedback count: {}",
                        threadId, toolFeedbacks.size());
            }

            log.debug("Agent execution completed successfully for threadId: {}", threadId);

            return new AgentInvokeResponse(
                    true,
                    "Execution completed",
                    toolFeedbacks
            );

        } catch (IllegalStateException e) {
            log.error("Agent execution failed for threadId: {}, error: {}", threadId, e.getMessage());
            return new AgentInvokeResponse(false, e.getMessage(), List.of());
        } catch (Exception e) {
            log.error("Unexpected error during agent execution for threadId: {}", threadId, e);
            return new AgentInvokeResponse(false, "Internal server error", List.of());
        }
    }

    private List<InterruptionMetadata.ToolFeedback> extractToolFeedbacks(NodeOutput nodeOutput) {

        if (nodeOutput == null) {
            return List.of();
        }

        try {
            // 尝试从 NodeOutput 的 state 中获取 InterruptionMetadata
            Object state = nodeOutput.state();

            if (state instanceof Map<?, ?> stateMap) {
                // 检查 state 中是否包含中断元数据
                Object interruptionData = stateMap.get("interruption");

                if (interruptionData instanceof InterruptionMetadata interruptionMetadata) {
                    // 保存元数据供后续反馈使用
                    String threadId = extractThreadId(nodeOutput);
                    if (threadId != null) {
                        map.put(threadId, interruptionMetadata);
                    }

                    // 返回工具反馈列表
                    return interruptionMetadata.toolFeedbacks();
                }
            }

            log.debug("No interruption metadata found in NodeOutput");
            return List.of();

        } catch (Exception e) {
            log.error("Failed to extract tool feedbacks from NodeOutput", e);
            return List.of();
        }
    }

    private String extractThreadId(NodeOutput nodeOutput) {

        try {
            // 尝试从 RunnableConfig 或 state 中获取 threadId
            Object state = nodeOutput.state();
            if (state instanceof Map<?, ?> stateMap) {
                Object threadId = stateMap.get("threadId");
                if (threadId instanceof String) {
                    return (String) threadId;
                }
            }
        } catch (Exception e) {
            log.warn("Failed to extract threadId from NodeOutput", e);
        }
        return null;
    }

    @PostMapping("/feedback")
    @ResponseBody
    public String feedback(@RequestBody List<Feedback> feedbacks,
                           @RequestParam("threadId") String threadId
    ) throws Exception {

        InterruptionMetadata metadata = map.get(threadId);
        if (metadata == null) {
            return "no metadata found";
        }
        if (metadata.toolFeedbacks().size() != feedbacks.size()) {
            return "feedback size not match";
        }

        InterruptionMetadata.Builder newBuilder = InterruptionMetadata.builder()
                                                                      .nodeId(metadata.node())
                                                                      .state(metadata.state());
        for (int i = 0; i < feedbacks.size(); i++) {
            var toolFeedback = metadata.toolFeedbacks().get(i);
            InterruptionMetadata.ToolFeedback.Builder editedFeedbackBuilder = InterruptionMetadata.ToolFeedback
                    .builder(toolFeedback);
            if (feedbacks.get(i).isApproved()) {
                editedFeedbackBuilder.result(InterruptionMetadata.ToolFeedback.FeedbackResult.APPROVED);
            } else {
                editedFeedbackBuilder.result(InterruptionMetadata.ToolFeedback.FeedbackResult.REJECTED)
                                     .description(feedbacks.get(i).feedback());
            }
            newBuilder.addToolFeedback(editedFeedbackBuilder.build());
        }
        RunnableConfig resumeRunnableConfig = RunnableConfig.builder().threadId(threadId)
                                                            .addMetadata(RunnableConfig.HUMAN_FEEDBACK_METADATA_KEY, newBuilder.build())
                                                            .build();
        reactAgent.invokeAndGetOutput("", resumeRunnableConfig);
        return "success";
    }

    @GetMapping("/index")
    public String index() {

        return "index";
    }

    public record Feedback(boolean isApproved, String feedback) {
    }

    public record AgentInvokeResponse(
            boolean success,
            String message,
            List<InterruptionMetadata.ToolFeedback> toolFeedbacks
    ) {}

}
