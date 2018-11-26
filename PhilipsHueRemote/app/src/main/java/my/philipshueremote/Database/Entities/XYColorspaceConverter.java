package my.philipshueremote.Database.Entities;

import android.arch.persistence.room.TypeConverter;

public class XYColorspaceConverter {
    @TypeConverter
    public static long floatArrayToLong(float xy[]) {
        long twoFloatsInLong = 0;
        twoFloatsInLong |= (((long)Float.floatToRawIntBits(xy[0])) << 32);
        twoFloatsInLong |= Float.floatToRawIntBits(xy[1]);

        return twoFloatsInLong;
    }

    @TypeConverter
    public static float[] longToFloatArray(long xy) {
        float floatX = Float.intBitsToFloat((int) (xy >> 32));
        float floatY = Float.intBitsToFloat((int) xy);

        return new float[] {floatX, floatY};
    }
}
