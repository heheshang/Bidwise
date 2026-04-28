package com.ssk.bidwise.util.tools;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.ToolCallback;

import java.util.function.BiFunction;

public interface Tool<I, O> extends BiFunction<I, ToolContext, O> {

    ToolCallback toolCallback();

}