package com.example.android.habittracker.data;

import android.provider.BaseColumns;

public final class ContractClass {

    private ContractClass() {
    }

    public static final class HabitEntry implements BaseColumns {

        public final static String TABLE_NAME = "habits";

        public final static String COLUMN_HABIT_NAME = "name";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_HABIT_DURATION = "time";

        public final static String COLUMN_HABIT_STATUS = "status";


        public static final int STATUS_UNKNOWN = 0;
        public static final int STATUS_GOOD = 1;
        public static final int STATUS_VERY_GOOD = 2;
        public static final int STATUS_BAD = 3;
        public static final int STATUS_VERY_BAD = 4;
    }
}
