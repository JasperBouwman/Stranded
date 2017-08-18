package com.Stranded.towers.events;

class TowerInfo {

    static String getTowerInfo(String s, String lvl) {

        if (s.startsWith("Speed lvl: ")) {
            switch (lvl) {
                case "1":
                    return "Speed Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Speed 1 for 1 second\n" +
                            "Next upgrade:\nCooldown: 30\nEffect: Speed 1 for 3 seconds";
                case "2":
                    return "Speed Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Speed 1 for 3 second\n" +
                            "Next upgrade:\nCooldown: 30\nEffect: Speed 1 for 5 second";
                case "3":
                    return "Speed Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Speed 1 for 5 second\n" +
                            "Next upgrade:\nCooldown: 30\nEffect: Speed 1 for 6 seconds";
                case "4":
                    return "Speed Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Speed 1 for 6 second\n" +
                            "Next upgrade:\nCooldown: 30\nEffect: Speed 1 for 7 seconds";
                case "5":
                    return "Speed Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Speed 1 for 7 second\n" +
                            "Next upgrade:\nCooldown: 30\nEffect: Speed 1 for 8 seconds";
                case "6":
                    return "Speed Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Speed 1 for 8 second\n" +
                            "Next upgrade:\nCooldown: 30\nEffect: Speed 1 for 9 seconds";
                case "7":
                    return "Speed Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Speed 1 for 9 second\n" +
                            "Next upgrade:\nCooldown: 30\nEffect: Speed 1 for 10 seconds";
                case "8":
                    return "Speed Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Speed 1 for 10 second\n" +
                            "Next upgrade:\nCooldown: 30\nEffect: Speed 2 for 10 seconds";
                case "MAX":
                    return "Speed Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Speed 2 for 10 second";
            }
        } else if (s.startsWith("Slow lvl: ")) {
            switch (lvl) {
                case "1":
                    return "Slowness Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Slowness 1 for 1 second\n" +
                            "Next upgrade:\nCooldown: 30\nEffect: Slowness 1 for 3 seconds";
                case "2":
                    return "Slowness Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Slowness 1 for 3 seconds\n" +
                            "Next upgrade:\nCooldown: 30\nEffect: Slowness 1 for 5 seconds";
                case "3":
                    return "Slowness Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Slowness 1 for 5 seconds\n" +
                            "Next upgrade:\nCooldown: 30\nEffect: Slowness 1 for 6 seconds";
                case "4":
                    return "Slowness Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Slowness 1 for 6 seconds\n" +
                            "Next upgrade:\nCooldown: 30\nEffect: Slowness 1 for 7 seconds";
                case "5":
                    return "Slowness Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Slowness 1 for 7 seconds\n" +
                            "Next upgrade:\nCooldown: 30\nEffect: Slowness 1 for 8 seconds";
                case "6":
                    return ("Slowness Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Slowness 1 for 8 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Slowness 1 for 9 seconds");
                case "7":
                    return ("Slowness Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Slowness 1 for 9 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Slowness 1 for 10 seconds");
                case "8":
                    return ("Slowness Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Slowness 1 for 10 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Slowness 2 for 10 seconds");
                case "MAX":
                    return ("Slowness Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Slowness 2 for 10 seconds");
            }
        } else if (s.startsWith("Regen lvl: ")) {
            switch (lvl) {
                case "1":
                    return ("Regeneration Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Regeneration 1 for 1 second\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Regeneration 1 for 2 seconds");
                case "2":
                    return ("Regeneration Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Regeneration 1 for 2 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Regeneration 1 for 3 seconds");
                case "3":
                    return ("Regeneration Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Regeneration 1 for 3 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Regeneration 1 for 3 seconds");
                case "4":
                    return ("Regeneration Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Regeneration 1 for 3 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Regeneration 1 for 4 seconds");
                case "5":
                    return ("Regeneration Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Regeneration 1 for 4 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Regeneration 1 for 4 seconds");
                case "6":
                    return ("Regeneration Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Regeneration 1 for 4 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Regeneration 1 for 5 seconds");
                case "7":
                    return ("Regeneration Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Regeneration 1 for 5 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Regeneration 1 for 5 seconds");
                case "8":
                    return ("Regeneration Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Regeneration 1 for 5 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Regeneration 1 for 6 seconds");
                case "MAX":
                    return ("Regeneration Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Regeneration 1 for 6 seconds");
            }
        } else if (s.startsWith("Haste lvl: ")) {
            switch (lvl) {
                case "1":
                    return ("Haste Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Haste 1 for 1 second\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Haste 1 for 2 seconds");
                case "2":
                    return ("Haste Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Haste 1 for 2 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Haste 1 for 3 seconds");
                case "3":
                    return ("Haste Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Haste 1 for 3 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Haste 1 for 4 seconds");
                case "4":
                    return ("Haste Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Haste 1 for 4 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Haste 1 for 5 seconds");
                case "5":
                    return ("Haste Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Haste 1 for 5 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Haste 1 for 6 seconds");
                case "6":
                    return ("Haste Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Haste 1 for 6 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Haste 1 for 7 seconds");
                case "7":
                    return ("Haste Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Haste 1 for 7 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Haste 1 for 7 seconds");
                case "8":
                    return ("Haste Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Haste 1 for 7 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Haste 2 for 8 seconds");
                case "MAX":
                    return ("Haste Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Haste 2 for 8 seconds");
            }
        } else if (s.startsWith("Wither lvl: ")) {
            switch (lvl) {
                case "1":
                    return ("Wither Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Wither 1 for 1 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Wither 1 for 2 seconds");
                case "2":
                    return ("Wither Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Wither 1 for 2 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Wither 1 for 3 seconds");
                case "3":
                    return ("Wither Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Wither 1 for 3 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Wither 1 for 4 seconds");
                case "4":
                    return ("Wither Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Wither 1 for 4 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Wither 1 for 4 seconds");
                case "5":
                    return ("Wither Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Wither 1 for 4 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Wither 1 for 5 seconds");
                case "6":
                    return ("Wither Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Wither 1 for 5 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Wither 1 for 5 seconds");
                case "7":
                    return ("Wither Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Wither 1 for 5 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Wither 1 for 6 seconds");
                case "8":
                    return ("Wither Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Wither 1 for 6 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Wither 1 for 7 seconds");
                case "MAX":
                    return ("Wither Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Wither 2 for 7 seconds");
            }
        } else if (s.startsWith("Hunger lvl: ")) {
            switch (lvl) {
                case "1":
                    return ("Hunger Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Hunger 1 for 1 second\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Hunger 1 for 2 seconds");
                case "2":
                    return ("Hunger Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Hunger 1 for 2 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Hunger 1 for 3 seconds");
                case "3":
                    return ("Hunger Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Hunger 1 for 3 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Hunger 1 for 4 seconds");
                case "4":
                    return ("Hunger Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Hunger 1 for 4 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Hunger 1 for 5 seconds");
                case "5":
                    return ("Hunger Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Hunger 1 for 5 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Hunger 1 for 6 seconds");
                case "6":
                    return ("Hunger Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Hunger 1 for 6 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Hunger 1 for 7 seconds");
                case "7":
                    return ("Hunger Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Hunger 1 for 7 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Hunger 1 for 8 seconds");
                case "8":
                    return ("Hunger Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Hunger 1 for 8 seconds\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: Hunger 1 for 10 seconds");
                case "MAX":
                    return ("Hunger Tower lvl " + lvl + ":\nCooldown: 30\nEffect: Hunger 2 for 10 seconds");
            }
        } else if (s.startsWith("Tnt lvl: ")) {
            switch (lvl) {
                case "1":
                    return ("Tnt Tower lvl " + lvl + ":\nCooldown: 45\nEffect: shoot 1 tnt\n") +
                            ("Next upgrade:\nCooldown: 40\nEffect: shoot 1 tnt");
                case "2":
                    return ("Tnt Tower lvl " + lvl + ":\nCooldown: 40\nEffect: shoot 1 tnt\n") +
                            ("Next upgrade:\nCooldown: 36\nEffect: shoot 1 tnt");
                case "3":
                    return ("Tnt Tower lvl " + lvl + ":\nCooldown: 36\nEffect: shoot 1 tnt\n") +
                            ("Next upgrade:\nCooldown: 33\nEffect: shoot 1 tnt");
                case "4":
                    return ("Tnt Tower lvl " + lvl + ":\nCooldown: 33\nEffect: shoot 1 tnt\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: shoot 1 tnt");
                case "5":
                    return ("Tnt Tower lvl " + lvl + ":\nCooldown: 30\nEffect: shoot 1 tnt\n") +
                            ("Next upgrade:\nCooldown: 27\nEffect: shoot 1 tnt");
                case "6":
                    return ("Tnt Tower lvl " + lvl + ":\nCooldown: 27\nEffect: shoot 1 tnt\n") +
                            ("Next upgrade:\nCooldown: 24\nEffect: shoot 1 tnt");
                case "7":
                    return ("Tnt Tower lvl " + lvl + ":\nCooldown: 24\nEffect: shoot 1 tnt\n") +
                            ("Next upgrade:\nCooldown: 22\nEffect: shoot 1 tnt");
                case "8":
                    return ("Tnt Tower lvl " + lvl + ":\nCooldown: 22\nEffect: shoot 1 tnt\n") +
                            ("Next upgrade:\nCooldown: 20\nEffect: shoot 1 tnt");
                case "MAX":
                    return ("Tnt Tower lvl " + lvl + ":\nCooldown: 20\nEffect: shoot 1 tnt");
            }
        } else if (s.startsWith("Arrow lvl: ")) {
            switch (lvl) {
                case "1":
                    return ("Arrow Tower lvl " + lvl + ":\nCooldown: 30\nEffect: shoot 5 arrows\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: shoot 10 arrows");
                case "2":
                    return ("Arrow Tower lvl " + lvl + ":\nCooldown: 30\nEffect: shoot 10 arrows\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: shoot 15 arrows");
                case "3":
                    return ("Arrow Tower lvl " + lvl + ":\nCooldown: 30\nEffect: shoot 15 arrows\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: shoot 20 arrows");
                case "4":
                    return ("Arrow Tower lvl " + lvl + ":\nCooldown: 30\nEffect: shoot 20 arrows\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: shoot 25 arrows");
                case "5":
                    return ("Arrow Tower lvl " + lvl + ":\nCooldown: 30\nEffect: shoot 25 arrows\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: shoot 30 arrows");
                case "6":
                    return ("Arrow Tower lvl " + lvl + ":\nCooldown: 30\nEffect: shoot 30 arrows\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: shoot 35 arrows");
                case "7":
                    return ("Arrow Tower lvl " + lvl + ":\nCooldown: 30\nEffect: shoot 35 arrows\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: shoot 40 arrows");
                case "8":
                    return ("Arrow Tower lvl " + lvl + ":\nCooldown: 30\nEffect: shoot 40 arrows\n") +
                            ("Next upgrade:\nCooldown: 30\nEffect: shoot 45 arrows");
                case "MAX":
                    return ("Arrow Tower lvl " + lvl + ":\nCooldown: 30\nEffect: shoot 45 arrows");
            }
        }
        return "";
    }
}
