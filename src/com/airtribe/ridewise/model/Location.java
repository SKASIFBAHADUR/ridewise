package com.airtribe.ridewise.model;

public enum Location {

    VIJAYAWADA(0, 0),
    GUNTUR(5, 3),
    TENALI(3, 2),
    MANGALAGIRI(2, 1),
    ELURU(-4, 6),
    MACHILIPATNAM(8, -2),
    ONGOLE(15, -8),
    NELLORE(25, -15),
    TIRUPATI(30, -20),
    KADAPA(22, -12),
    KURNOOL(18, 15),
    ANANTAPUR(10, 20),
    HINDUPUR(8, 25),
    CHITTOOR(28, -25),
    VISAKHAPATNAM(-20, 25),
    VIZIANAGARAM(-18, 28),
    SRIKAKULAM(-22, 35),
    RAJAHMUNDRY(-10, 15),
    KAKINADA(-12, 18),
    AMALAPURAM(-11, 12),
    BHIMAVARAM(-6, 8),
    TADEPALLIGUDEM(-5, 10),
    NARASARAOPET(7, 6),
    CHILAKALURIPET(8, 4),
    MARKAPUR(12, 5),
    NANDYAL(16, 12),
    ADONI(20, 18),
    PRODDATUR(24, -10),
    MADANAPALLE(26, -18),
    TUNI(-15, 20);

    private final int x;
    private final int y;

    Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
