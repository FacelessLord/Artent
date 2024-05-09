package faceless.artent.playerData.api;

public record MoneyPouch(long bronze, long silver, long gold) {
    public long[] asArray() {
        return new long[]{bronze, silver, gold};
    }

    public long asLong() {
        return gold * 10000 + silver * 100 + bronze;
    }

    public boolean greaterOrEqual(MoneyPouch than) {
        return this.asLong() >= than.asLong();
    }

    public static MoneyPouch fromLong(long money) {
        return new MoneyPouch(money % 100, (money / 100) % 100, money / 10000);
    }
}
