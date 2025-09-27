package org.daylight.numismaticscalculator.ui.elements;

public enum MainDistribution {
    START,
    CENTER,
    END,
    SPACE_BETWEEN,  // equal gaps between elements, no gaps at edges
    SPACE_AROUND,   // equal gaps around elements (half-size gaps at edges)
    SPACE_EVENLY,    // equal gaps everywhere, including edges
    FILL
}
