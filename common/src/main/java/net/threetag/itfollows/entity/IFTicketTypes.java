package net.threetag.itfollows.entity;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.TicketType;
import net.threetag.itfollows.ItFollows;

public class IFTicketTypes {

    public static final DeferredRegister<TicketType> TICKET_TYPES = DeferredRegister.create(ItFollows.MOD_ID, Registries.TICKET_TYPE);

    public static final RegistrySupplier<TicketType> THE_ENTITY = TICKET_TYPES.register("the_entity", () -> new TicketType(40L, false, TicketType.TicketUse.LOADING_AND_SIMULATION));

}
