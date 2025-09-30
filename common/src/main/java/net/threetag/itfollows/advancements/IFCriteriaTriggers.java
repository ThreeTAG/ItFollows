package net.threetag.itfollows.advancements;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.registries.Registries;
import net.threetag.itfollows.ItFollows;

import java.util.Optional;

public class IFCriteriaTriggers {

    public static final DeferredRegister<CriterionTrigger<?> > CRITERIA_TRIGGERS = DeferredRegister.create(ItFollows.MOD_ID, Registries.TRIGGER_TYPE);

    public static final RegistrySupplier<PlayerTrigger> RECEIVED_CURSE = CRITERIA_TRIGGERS.register("received_curse", PlayerTrigger::new);
    public static final RegistrySupplier<PlayerTrigger> BROKE_CURSE = CRITERIA_TRIGGERS.register("broke_curse", PlayerTrigger::new);
    public static final RegistrySupplier<PlayerTrigger> PASSED_ON_CURSE = CRITERIA_TRIGGERS.register("passed_on_curse", PlayerTrigger::new);
    public static final RegistrySupplier<PlayerTrigger> RETURNED_CURSE = CRITERIA_TRIGGERS.register("returned_curse", PlayerTrigger::new);

    public static Criterion<PlayerTrigger.TriggerInstance> receivedCurse(EntityPredicate.Builder entity) {
        return RECEIVED_CURSE.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.of(EntityPredicate.wrap(entity.build()))));
    }

    public static Criterion<PlayerTrigger.TriggerInstance> brokeCurse(EntityPredicate.Builder entity) {
        return BROKE_CURSE.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.of(EntityPredicate.wrap(entity.build()))));
    }

    public static Criterion<PlayerTrigger.TriggerInstance> passedOnCurse(EntityPredicate.Builder entity) {
        return PASSED_ON_CURSE.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.of(EntityPredicate.wrap(entity.build()))));
    }

    public static Criterion<PlayerTrigger.TriggerInstance> returnedCurse(EntityPredicate.Builder entity) {
        return RETURNED_CURSE.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.of(EntityPredicate.wrap(entity.build()))));
    }

}
