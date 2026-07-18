package com.hedge.hedges_bestiary.client.particle;

import com.hedge.hedges_bestiary.registry.HBParticles;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Locale;

public class BansheeScreamParticleOptions implements ParticleOptions {
    private final float xRot;
    private final float yRot;

    public BansheeScreamParticleOptions(float xRot, float yRot) {
        this.xRot = xRot;
        this.yRot = yRot;
    }

    public float getXRot() {
        return this.xRot;
    }
    public float getYRot() {
        return this.yRot;
    }
    public static final Codec<BansheeScreamParticleOptions> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(
                            Codec.FLOAT.fieldOf("xRot").forGetter(BansheeScreamParticleOptions::getXRot),
                            Codec.FLOAT.fieldOf("yRot").forGetter(BansheeScreamParticleOptions::getYRot)
                    ).apply(instance, BansheeScreamParticleOptions::new)
            );

    public static final Deserializer<BansheeScreamParticleOptions> DESERIALIZER =
            new Deserializer<>() {
            @Override
            public BansheeScreamParticleOptions fromCommand(
                    ParticleType<BansheeScreamParticleOptions> type,
                    StringReader reader
            ) throws CommandSyntaxException {
                reader.expect(' ');
                float xRot = reader.readFloat();
                reader.expect(' ');
                float yRot = reader.readFloat();
                return new BansheeScreamParticleOptions(xRot, yRot);
            }

            @Override
            public BansheeScreamParticleOptions fromNetwork(
                    ParticleType<BansheeScreamParticleOptions> type,
                    FriendlyByteBuf buf
            )
                {
                return new BansheeScreamParticleOptions(buf.readFloat(), buf.readFloat());
                }
            };


    @Override
    public ParticleType<?> getType() {
        return HBParticles.BANSHEE_SCREAM.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        pBuffer.writeFloat(this.xRot);
        pBuffer.writeFloat(this.yRot);

    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), this.xRot, this.yRot);
    }
}
