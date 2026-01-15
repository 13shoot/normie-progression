package io.github._13shoot.normieprogression.chronicle;

import io.github._13shoot.normieprogression.mark.MarkData;
import io.github._13shoot.normieprogression.mark.MarkType;

import java.util.List;
import java.util.UUID;

public class MarkChronicleHook {

    public static void onMarkFirstObtained(UUID player, MarkType type) {

        switch (type) {

            case SURVIVAL -> ChronicleService.addMarkChronicle(
                    player,
                    "mark.survival",
                    "Still Here",
                    List.of(
                            "Nothing celebrated your survival.",
                            "So you did it yourself."
                    )
            );

            case HUNGER -> ChronicleService.addMarkChronicle(
                    player,
                    "mark.hunger",
                    "The Quiet Hunger",
                    List.of(
                            "There was a time when food mattered more than plans.",
                            "Silence ached longer than expected."
                    )
            );

            case PERSISTENCE -> ChronicleService.addMarkChronicle(
                    player,
                    "mark.persistence",
                    "Unwilling to Quit",
                    List.of(
                            "You failed more than once.",
                            "You kept going anyway."
                    )
            );

            case BLOOD -> ChronicleService.addMarkChronicle(
                    player,
                    "mark.blood",
                    "Too Close to the End",
                    List.of(
                            "Death visited more than once.",
                            "You answered anyway."
                    )
            );

            case RECOGNITION -> ChronicleService.addMarkChronicle(
                    player,
                    "mark.recognition",
                    "The World Took Notice",
                    List.of(
                            "You reached places few return from.",
                            "The world remembered your name."
                    )
            );

            case LOSS -> ChronicleService.addMarkChronicle(
                    player,
                    "mark.loss",
                    "What Was Lost",
                    List.of(
                            "Something important slipped away.",
                            "You carried the weight forward."
                    )
            );

            case TRADE -> ChronicleService.addMarkChronicle(
                    player,
                    "mark.trade",
                    "Coins in Motion",
                    List.of(
                            "Money moved faster than trust.",
                            "The market watched quietly."
                    )
            );

            case COLD -> ChronicleService.addMarkChronicle(
                    player,
                    "mark.cold",
                    "Cold Accepted You",
                    List.of(
                            "The cold tried to push you out.",
                            "It stopped trying."
                    )
            );

            case INFLUENCE -> ChronicleService.addMarkChronicle(
                    player,
                    "mark.influence",
                    "Words That Moved Things",
                    List.of(
                            "You spoke.",
                            "The world listened."
                    )
            );

            case FEAR -> ChronicleService.addMarkChronicle(
                    player,
                    "mark.fear",
                    "The Near End",
                    List.of(
                            "You came close to disappearing.",
                            "The memory stayed."
                    )
            );

            case WITNESS -> ChronicleService.addMarkChronicle(
                    player,
                    "mark.witness",
                    "Someone Remembers",
                    List.of(
                            "You saw others disappear.",
                            "You stayed to remember."
                    )
            );

            case FAVOR -> ChronicleService.addMarkChronicle(
                    player,
                    "mark.favor",
                    "A Brief Smile",
                    List.of(
                            "Something went right.",
                            "No one knows why."
                    )
            );

            default -> {}
        }
    }
}
