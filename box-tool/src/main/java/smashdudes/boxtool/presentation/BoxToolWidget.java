package smashdudes.boxtool.presentation;

import smashdudes.core.ImGuiWidget;
import smashdudes.boxtool.logic.BoxToolContext;

public abstract class BoxToolWidget extends ImGuiWidget
{
    protected BoxToolContext context;

    public BoxToolWidget(String title, int windowFlags, BoxToolContext context)
    {
        super(title, windowFlags);
        this.context = context;
    }
}
