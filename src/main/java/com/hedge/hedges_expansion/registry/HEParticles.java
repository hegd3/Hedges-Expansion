package com.hedge.hedges_expansion.registry;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.particle.SmokeParticle;
import com.hedge.hedges_expansion.client.particle.SmokeParticleOptions;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HEParticles {
    public static final DeferredRegister<ParticleType<?>> DEF_REG = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, HedgesExpansion.MODID);
    public static final RegistryObject<SimpleParticleType> MURK_CHARGE = DEF_REG.register("murk_charge", ()-> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> MURK_CHARGE_SHOOT = DEF_REG.register("murk_charge_shoot", ()-> new SimpleParticleType(false));

    public static final RegistryObject<ParticleType<SmokeParticleOptions>> SMOKE = DEF_REG.register("smoke", ()-> new ParticleType<>(true, SmokeParticleOptions.DESERIALIZER) {
        @Override
        public Codec<SmokeParticleOptions> codec() {
            return SmokeParticleOptions.CODEC;
        }

    });
    public static void register(IEventBus eventbus) {
        DEF_REG.register(eventbus);
    }

}
