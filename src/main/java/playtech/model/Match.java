package main.java.playtech.model;

import java.util.UUID;

public record Match(UUID matchId, double returnRateA, double returnRateB,
                    main.java.playtech.model.Match.Result result) {
    public enum Result {
        A,
        B,
        DRAW
    }
}
