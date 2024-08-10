/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.heavenmine.hmtopup.modal;

public enum CardPrice {
    _10K("10.000", 1),
    _20K("20.000", 2),
    _30K("30.000", 3),
    _50K("50.000", 4),
    _100K("100.000", 5),
    _200K("200.000", 6),
    _300K("300.000", 7),
    _500K("500.000", 8),
    _1M("1.000.000", 9),
    UNKNOWN("0", -1);

    private final String price;
    private final int id;

    private CardPrice(String price, int id) {
        this.price = price;
        this.id = id;
    }

    public static CardPrice getPrice(String price) {
        for (CardPrice a : CardPrice.values()) {
            if (!a.price.equals(price)) continue;
            return a;
        }
        return UNKNOWN;
    }

    public String getPrice() {
        return this.price;
    }

    public String getId() {
        return this.id + "";
    }
}

