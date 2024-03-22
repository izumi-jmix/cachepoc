package org.izumi.jmix.cachepoc.screen.chairheader;

import io.jmix.ui.screen.*;
import org.izumi.jmix.cachepoc.entity.ChairHeader;

@UiController("ChairHeader.browse")
@UiDescriptor("chair-header-browse.xml")
@LookupComponent("chairHeadersTable")
public class ChairHeaderBrowse extends StandardLookup<ChairHeader> {
}