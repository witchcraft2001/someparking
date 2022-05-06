package com.someparking.androidapp.utils

import com.someparking.androidapp.common.Contants.MILLIS_IN_SEC
import com.someparking.androidapp.storage.preferences.Preferences
import java.lang.StringBuilder
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TimeUtils @Inject constructor(
    private val preferences: Preferences
) {
    fun longSecondsToTime(time: Long): String {
        val milliseconds = time * MILLIS_IN_SEC
        return longMillisToTime(milliseconds)
    }

    fun longMillisToTime(time: Long, withSeconds: Boolean = true): String {
        val sb = StringBuilder()
        val days = TimeUnit.MILLISECONDS.toDays(time)
        if (days > 0) {
            sb.append(String.format("%02d д.", days))
        }
        var temp = time - TimeUnit.DAYS.toMillis(days)
        val hours = TimeUnit.MILLISECONDS.toHours(temp)
        if (hours > 0) {
            sb.append(String.format(TIME_PART_FORMAT, hours))
        }
        temp -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(temp)
        temp -= TimeUnit.MINUTES.toMillis(minutes)
        if (withSeconds) {
            sb.append(String.format(TIME_PART_FORMAT, minutes))
        } else {
            sb.append(String.format(TIME_PART_ENDS_FORMAT, minutes))
        }

        if (withSeconds) {
            val seconds = TimeUnit.MILLISECONDS.toSeconds(temp)
            sb.append(String.format(TIME_PART_ENDS_FORMAT, seconds))
        }

        return sb.toString()
    }

    fun millisToStringWithServerCorrection(time: Long) : String {
        val timeDelta = preferences.getServerTimeDeltaSync()
        return millisToString(time - timeDelta)
    }

    fun millisToString(time: Long): String {
        val calendar = Calendar.getInstance().apply {
            setTime(Date(time))
        }
        return String.format(
            "%04d-%02d-%02d %02d:%02d:%02d",
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND)
        )
    }

    companion object {
        private const val TIME_PART_FORMAT = "%02d:"
        private const val TIME_PART_ENDS_FORMAT = "%02d"
    }
}