package com.igorivkin.testprogramrunner.util;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StringBuilderLogReader extends ResultCallback.Adapter<Frame> {

    private StringBuilder builder;

    public StringBuilderLogReader(StringBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void onNext(Frame item) {
        builder.append(new String(item.getPayload()));
        super.onNext(item);
    }
}
