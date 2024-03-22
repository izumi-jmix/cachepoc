package org.izumi.jmix.cachepoc.screen.division;

import io.jmix.ui.screen.*;
import org.izumi.jmix.cachepoc.entity.Division;

@UiController("Division.edit")
@UiDescriptor("division-edit.xml")
@EditedEntityContainer("divisionDc")
public class DivisionEdit extends StandardEditor<Division> {
}