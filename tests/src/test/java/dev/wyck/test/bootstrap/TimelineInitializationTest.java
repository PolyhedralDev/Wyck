package dev.wyck.test.bootstrap;

import dev.wyck.keys.ResourceKey;
import dev.wyck.level.dimension.timeline.Timeline;
import dev.wyck.level.dimension.timeline.types.ReferencedTimeline;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Constructing a timeline reference before {@link Timeline} is initialized loads the impl class, which
 * initializes {@link Timeline}, whose constants construct references through the same provider. That
 * nested construction used to fail with "Recursive update". Relies on {@code forkEvery = 1}: nothing
 * else in this JVM may touch {@link Timeline} first.
 */
@NullMarked
@ExtendWith(MinecraftBootstrap.class)
class TimelineInitializationTest {

    @Test
    void aReferenceConstructedBeforeTimelineInitializes() {
        ReferencedTimeline reference = ReferencedTimeline.of(ResourceKey.minecraft("day"));

        assertEquals(ResourceKey.minecraft("day"), reference.key());
        assertEquals(ResourceKey.minecraft("day"), Timeline.OVERWORLD_DAY.key());
    }
}
