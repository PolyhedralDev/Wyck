package dev.wyck.environment.sounds;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Internal
final class SoundEventHolders {

    private SoundEventHolders() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Holds a sound event by its registry reference when the registered event is the same one,
     * so vanilla sounds encode as their key; anything else is held directly.
     * @param sound the sound event
     * @return the registered reference, or a direct holder
     */
    static Holder<net.minecraft.sounds.SoundEvent> holder(SoundEvent sound) {
        net.minecraft.sounds.SoundEvent event = sound.asHandle();
        return BuiltInRegistries.SOUND_EVENT.get(event.location())
            .filter(reference -> reference.value().equals(event))
            .<Holder<net.minecraft.sounds.SoundEvent>>map(reference -> reference)
            .orElseGet(() -> Holder.direct(event));
    }
}
