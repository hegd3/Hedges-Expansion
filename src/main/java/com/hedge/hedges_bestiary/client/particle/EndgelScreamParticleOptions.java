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

public class EndgelScreamParticleOptions implements ParticleOptions {
    private final float xRot;
    private final float yRot;
    private final float quadSize;
    public EndgelScreamParticleOptions(float xRot, float yRot, float quadSize) {
        this.xRot = xRot;
        this.yRot = yRot;
        this.quadSize = quadSize;
    }

    public float getXRot() {
        return this.xRot;
    }
    public float getYRot() {
        return this.yRot;
    }
    public float getQuadSize() {return this.quadSize;}
    public static final Codec<EndgelScreamParticleOptions> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(
                            Codec.FLOAT.fieldOf("xRot").forGetter(EndgelScreamParticleOptions::getXRot),
                            Codec.FLOAT.fieldOf("yRot").forGetter(EndgelScreamParticleOptions::getYRot),
                            Codec.FLOAT.fieldOf("quadSize").forGetter(EndgelScreamParticleOptions::getQuadSize)

                            ).apply(instance, EndgelScreamParticleOptions::new)
            );

    public static final Deserializer<EndgelScreamParticleOptions> DESERIALIZER =
            new Deserializer<>() {
            @Override
            public EndgelScreamParticleOptions fromCommand(
                    ParticleType<EndgelScreamParticleOptions> type,
                    StringReader reader
            ) throws CommandSyntaxException {
                reader.expect(' ');
                float xRot = reader.readFloat();
                reader.expect(' ');
                float yRot = reader.readFloat();
                reader.expect(' ');
                float quadSize = reader.readFloat();
                return new EndgelScreamParticleOptions(xRot, yRot, quadSize);
            }

            @Override
            public EndgelScreamParticleOptions fromNetwork(
                    ParticleType<EndgelScreamParticleOptions> type,
                    FriendlyByteBuf buf
            )
                {
                return new EndgelScreamParticleOptions(buf.readFloat(), buf.readFloat(), buf.readFloat());
                }
            };


    @Override
    public ParticleType<?> getType() {
        return HBParticles.ENDGEL_SCREAM.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        pBuffer.writeFloat(this.xRot);
        pBuffer.writeFloat(this.yRot);
        pBuffer.writeFloat(this.quadSize);

    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), this.xRot, this.yRot, this.quadSize);
    }
}
