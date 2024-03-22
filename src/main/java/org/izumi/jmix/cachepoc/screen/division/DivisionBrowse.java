package org.izumi.jmix.cachepoc.screen.division;

import io.jmix.ui.screen.*;
import org.izumi.jmix.cachepoc.entity.Division;

@UiController("Division.browse")
@UiDescriptor("division-browse.xml")
@LookupComponent("divisionsTable")
public class DivisionBrowse extends StandardLookup<Division> {
}