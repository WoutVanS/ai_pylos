package be.kuleuven.pylos.battle;

import be.kuleuven.pylos.player.PylosPlayer;

import java.util.Objects;

public class RRCombination{
    public Class<? extends PylosPlayer> c1;
    public Class<? extends PylosPlayer> c2;
    public BattleResult battleResult;

    public RRCombination(Class<? extends PylosPlayer> c1, Class<? extends PylosPlayer> c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RRCombination that = (RRCombination) o;
        return (Objects.equals(c1, that.c1) && Objects.equals(c2, that.c2))
                || (Objects.equals(c1, that.c2) && Objects.equals(c2, that.c1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(c1, c2) + Objects.hash(c2, c1);
    }
}
