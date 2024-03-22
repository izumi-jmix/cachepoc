package org.izumi.jmix.cachepoc.screen.generator;

import java.util.Collection;
import java.util.function.Consumer;

import io.jmix.core.DataManager;
import io.jmix.core.FetchPlanBuilder;
import io.jmix.core.Metadata;
import io.jmix.core.SaveContext;
import io.jmix.core.metamodel.model.MetaClass;
import io.jmix.data.Sequence;
import io.jmix.data.Sequences;
import io.jmix.ui.component.Button;
import io.jmix.ui.screen.Screen;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.izumi.jmix.cachepoc.entity.ChairActual;
import org.izumi.jmix.cachepoc.entity.ChairHeader;
import org.izumi.jmix.cachepoc.entity.ChairHistory;
import org.izumi.jmix.cachepoc.entity.ChairLine;
import org.izumi.jmix.cachepoc.entity.Division;
import org.izumi.jmix.cachepoc.entity.HasOrder;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("GeneratorScreen")
@UiDescriptor("generator-screen.xml")
public class GeneratorScreen extends Screen {

    @Autowired
    private DataManager dataManager;
    @Autowired
    private Metadata metadata;
    @Autowired
    private Sequences sequences;

    @Subscribe("generateBtn")
    public void onGenerateBtnClick(final Button.ClickEvent event) {
        final int batchSize = 500;
        final int toGenerate = batchSize + 2;

        final RandomList<Division> divisions = new RandomList<>(all(Division.class));
        if (divisions.isEmpty()) {
            throw new IllegalStateException("No divisions in system");
        }

        final SaveContext context = new SaveContext()
                .setDiscardSaved(true);

        final ChairHeader header = metadata.create(ChairHeader.class);
        header.setOrder((int) sequences.createNextValue(sequence(ChairHeader.class)));
        header.setName(name(header));
        context.saving(header);
        for (int i = 0; i < toGenerate; i++) {
            final ChairLine line = metadata.create(ChairLine.class);
            line.setOrder(i + 1);
            line.setName(name(line));
            line.setHeader(header);
            //line.setDivision(divisions.next(null));

            final ChairHistory history = metadata.create(ChairHistory.class);
            history.setOrder((int) sequences.createNextValue(sequence(ChairHistory.class)));
            history.setName(name(history));
            history.setBranch(divisions.next(null));

            final ChairActual actual = metadata.create(ChairActual.class);
            actual.setOrder(history.getOrder());
            actual.setName(name(actual));
            actual.setBranch(divisions.next(null));
            history.setActual(actual);

            line.setHistory(history);

            context.saving(line, history, actual);
        }

        dataManager.save(context);
    }

    private Sequence sequence(Class<?> clazz) {
        return Sequence.withName(clazz.getName().replaceAll("\\.", "_"));
    }

    private String name(HasOrder hasOrder) {
        return metadata.getClass(hasOrder.getClass()).getName() + " " + hasOrder.getOrder();
    }

    private <T> T any(Class<T> clazz) {
        return any(clazz, b -> b.add("id"));
    }

    private <T> T any(Class<T> clazz, Consumer<FetchPlanBuilder> configurer) {
        final MetaClass metaClass = metadata.getClass(clazz);
        return dataManager.load(clazz)
                .query("SELECT e FROM " + metaClass.getName() + " e")
                .maxResults(1)
                .fetchPlan(configurer)
                .optional()
                .orElseThrow();
    }

    private <T> Collection<T> all(Class<T> clazz) {
        return all(clazz, b -> b.add("id"));
    }

    private <T> Collection<T> all(Class<T> clazz, Consumer<FetchPlanBuilder> configurer) {
        return dataManager.load(clazz)
                .all()
                .fetchPlan(configurer)
                .list();
    }
}