package my.philipshueremote.Database.Entities;

import android.arch.persistence.room.TypeConverter;

public class LampLightStatesConverter {

    @TypeConverter
    public static int stateToInteger(final LampLightStates state) {
        return state.ordinal();
    }

    @TypeConverter
    public static LampLightStates integerToState(final int state) {
        return LampLightStates.values()[state];
    }
}
