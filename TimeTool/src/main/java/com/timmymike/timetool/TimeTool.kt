package com.timmymike.timetool

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object TimeUnits {
    /** 一秒 */
    val oneSec = 1000L

    /** 一分 */
    val oneMin = oneSec * 60L

    /** 一小時 */
    val oneHour = oneMin * 60L

    /** 一天 */
    val oneDay = oneHour * 24L

    /** 一周 */
    val oneWeek = oneDay * 7L

    /** 一月 */
    val oneMonth = oneDay * 30L

    /** 一年 */
    val oneYear = oneMonth * 12L
}

val calenderInst: Calendar
    get() = Calendar.getInstance()

/**
 * 取得現在時間
 * @author wang
 * @date 2016/3/1 下午2:22:39
 * @version
 */
val nowTime: Long
    get() = System.currentTimeMillis()

fun String.toDate(format: String): Date? {
    var milliseconds: Long = -1
    try {
        val f = SimpleDateFormat(format, Locale.getDefault())
        return f.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}

fun String.toDateNonNull(format: String, default: Date = Date(0)): Date {
    return toDate(format) ?: default
}

fun Long.toDate(): Date {
    return Date(this)
}

fun Long.toInterval(): Long {
    return (Date().time - this)
}

fun Date.toString(format: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    sdf.timeZone = timeZone
    return sdf.format(this)
}

fun Date.toYMDHMS(needShowMillsecond: Boolean = true, timeZone: TimeZone = TimeZone.getDefault()): String {
    return if (needShowMillsecond) this.toString("yyyy-MM-dd HH:mm:ss.SSS", timeZone) else this.toString("yyyy-MM-dd HH:mm:ss", timeZone)
}

fun Long.toYMDHMS(needShowMillsecond: Boolean = true, timeZone: TimeZone = TimeZone.getDefault()): String {
    return this.toDate().toYMDHMS(needShowMillsecond, timeZone)
}

fun Date.toYMD(): String {
    return this.toString("yyyyMMdd")
}

val Date.dateInt: Int
    get() = toString("yyyyMMdd").toInt()

val Date.calender: Calendar
    get() {
        val calender = calenderInst
        calender.time = this
        return calender
    }

val Calendar.year: Int
    get() = get(Calendar.YEAR)

val Calendar.month: Int
    get() = get(Calendar.MONTH) + 1

val Calendar.dayOfMonth: Int
    get() = get(Calendar.DAY_OF_MONTH)

val Calendar.dayOfWeek: Int
    get() = get(Calendar.DAY_OF_WEEK) - 1

val Calendar.dayOfYear: Int
    get() = get(Calendar.DAY_OF_YEAR)

val Calendar.hour: Int
    get() = get(Calendar.HOUR)

val Calendar.hourOfDay: Int
    get() = get(Calendar.HOUR_OF_DAY)

val Calendar.minute: Int
    get() = get(Calendar.MINUTE)

val Calendar.second: Int
    get() = get(Calendar.SECOND)

val Calendar.millisecond: Int
    get() = get(Calendar.MILLISECOND)


/**
 * 取得年
 * @author Timmy
 * @date 2020/05/15
 */
fun Date.getYearSerial(): Int {
    val calender = calenderInst
    calender.time = this
    return calender.get(Calendar.YEAR)
}

/**
 * 取得月
 * @author Timmy
 * @date 2020/05/15
 */
fun Date.getMonthSerial(): Int {
    val calender = calenderInst
    calender.time = this
    return calender.get(Calendar.MONTH) + 1
}

/**
 * 藉由count取得一個月的開始時間(數日期時使用)(比如說下三個月的第一天是什麼時候)
 * @author Timmy
 * @date 2020/05/15
 * */
fun Date.getMonthStartByCount(count: Int): Date {
    return this.getFieldByCount(Calendar.MONTH, count).getMonthFirstDay().getStartOfDay()
}

/**
 * 藉由count取得一個月的結束時間(數日期時使用)(比如說下兩個月的最後一天是什麼時候)
 * @author Timmy
 * @date 2020/05/15
 * */
fun Date.getMonthEndByCount(count: Int): Date {
    return this.getFieldByCount(Calendar.MONTH, count).getMonthEndDay().getEndOfDay()
}

/**
 * 藉由field與count取得正確日期資料
 * @author wang
 * @date 2020/05/15
 * */
fun Date.getFieldByCount(field: Int, count: Int): Date {
    val calender = calenderInst
    calender.time = Date()
    calender.add(field, count)
    return calender.time
}

/**
 * 取得一天的起始時間
 * @author wang
 * @date 2020/02/07
 * */
fun Date.getStartOfDay(): Date {
    val calender = calenderInst
    calender.time = this
    calender.set(Calendar.HOUR_OF_DAY, 0)
    calender.set(Calendar.MINUTE, 0)
    calender.set(Calendar.SECOND, 0)
    calender.set(Calendar.MILLISECOND, 0)
    return calender.time
}

/**
 * 取得一天的結束時間
 * @author wang
 * @date 2020/02/07
 * */
fun Date.getEndOfDay(): Date {
    val calender = calenderInst
    calender.time = this
    calender.set(Calendar.HOUR_OF_DAY, 23)
    calender.set(Calendar.MINUTE, 59)
    calender.set(Calendar.SECOND, 59)
    calender.set(Calendar.MILLISECOND, 999)
    return calender.time
}

/**
 * 取得一週的第一天
 * @author wang
 * @date 2019/03/04
 * @param week 一週第一天 預設星期天
 * @param calendarUse 日曆的部分由於在「每月第一天是一周的第一天」的情況中，會顯示在第一行，因此多了這個變數判斷是否是日曆使用
 *                    若是，且上述的情況發生，將會取前一周，以讓其顯示在第二行(為了美觀)
 * */
fun Date.getWeekFirstDay(week: Int = Calendar.SUNDAY, calendarUse: Boolean = false): Date {
    val calender = calenderInst
    calender.time = this
    calender.set(Calendar.DAY_OF_WEEK, week)
    //Timmy修改：確保取得的時間一定小於傳入值
    return if (calendarUse && calender.time.time == this.time)
        Date(calender.time.time - TimeUnits.oneWeek)
    else
        calender.time
}

/**
 * 取得一週的最後一天
 * @author wang
 * @date 2019/03/04
 * @param week 一週最後一天 預設星期六
 * */
fun Date.getWeekEndDay(week: Int = Calendar.SATURDAY): Date {
    val calender = calenderInst
    calender.time = this
    calender.set(Calendar.DAY_OF_WEEK, week)
    //Timmy修改：確保取得的時間一定大於傳入值
    return if (calender.time.time >= this.time)
        calender.time
    else
        Date(calender.time.time + TimeUnits.oneWeek)
}


/**
 * 取得星期
 * @author wang
 * @date 2016/3/1 下午2:22:39
 * @version
 */
fun Date.getDayOfWeek(): Int {
    val date = this
    val calender = calenderInst
    calender.time = date
    return calender.get(Calendar.DAY_OF_WEEK) - 1
}

/**
 * 取得一個月的第一天
 * @author wang
 * @date 2019/03/04
 * */
fun Date.getMonthFirstDay(): Date {
    val calender = calenderInst
    calender.set(Calendar.MONTH, 0)
    calender.time = this
    calender.set(Calendar.DAY_OF_MONTH, calender.getActualMinimum(Calendar.DAY_OF_MONTH))
    return calender.time
}

/**
 * 取得一個月的第一天
 * @author wang
 * @date 2019/03/04
 * @param year 取直年份
 * @param month 取直月份
 * */
fun Date.getMonthFirstDay(year: Int, month: Int): Date {
    val calender = calenderInst
    calender.set(Calendar.YEAR, year)
    calender.set(Calendar.MONTH, month)
    calender.set(Calendar.DAY_OF_MONTH, calender.getActualMinimum(Calendar.DAY_OF_MONTH))
    return calender.time
}

/**
 * 取得一個月的最後一天
 * @author wang
 * @date 2019/03/04
 * */
fun Date.getMonthEndDay(): Date {
    val calender = calenderInst
    calender.time = this
    calender.set(Calendar.DAY_OF_MONTH, calender.getActualMaximum(Calendar.DAY_OF_MONTH))
    return calender.time
}

/**
 * 取得一個月的第一天
 * @author wang
 * @date 2019/03/04
 * @param year 取直年份
 * @param month 取直月份
 * */
fun Date.getMonthEndDay(year: Int, month: Int): Date {
    val calender = calenderInst
    calender.set(Calendar.YEAR, year)
    calender.set(Calendar.MONTH, month)
    calender.set(Calendar.DAY_OF_MONTH, calender.getActualMaximum(Calendar.DAY_OF_MONTH))
    return calender.time
}

/**
 * 取得一季的第一天
 * @author wang
 * @date 2019/03/04
 * */
fun Date.getSeasonFirstDay(): Date {
    val calender = calenderInst
    calender.time = this
    val year = calender.get(Calendar.YEAR)
    val month = calender.get(Calendar.MONTH)
    val season = when (month) {
        in 0..2 -> 0
        in 3..5 -> 1
        in 6..8 -> 2
        in 9..11 -> 3
        else -> 0
    }
    return getMonthFirstDay(year, season * 3)
}

/**
 * 取得一季的最後一天
 * @author wang
 * @date 2019/03/04
 * @param milliseconds 取直時間
 * */
fun Date.getSeasonEndDay(): Date {
    val calender = calenderInst
    calender.time = this
    val year = calender.get(Calendar.YEAR)
    val month = calender.get(Calendar.MONTH)
    val season = when (month) {
        in 0..2 -> 0
        in 3..5 -> 1
        in 6..8 -> 2
        in 9..11 -> 3
        else -> 0
    }
    return getMonthEndDay(year, season * 3 + 2)
}

/**
 * 取得一年的第一天
 * @author Hsieh
 * @date 2019/04/09
 * @param date 取值日期
 * */
fun Date.getYearFirstDay(): Date {
    val calender = calenderInst
    calender.time = this
    calender.set(Calendar.YEAR, calender.get(Calendar.YEAR))
    calender.set(Calendar.MONTH, calender.getActualMinimum(Calendar.MONTH))
    calender.set(Calendar.DAY_OF_MONTH, calender.getActualMinimum(Calendar.DAY_OF_MONTH))
    calender.set(Calendar.HOUR_OF_DAY, calender.getActualMinimum(Calendar.HOUR_OF_DAY))
    calender.set(Calendar.MINUTE, calender.getActualMinimum(Calendar.MINUTE))
    calender.set(Calendar.SECOND, calender.getActualMinimum(Calendar.SECOND))
    calender.set(Calendar.MILLISECOND, calender.getActualMinimum(Calendar.MILLISECOND))
    return calender.time
}

/**
 * 取得一年的最後一天
 * @author Hsieh
 * @date 2019/04/09
 * @param date 取值日期
 * */
fun Date.getYearEndDay(date: Int): Date {
    val calender = calenderInst
    calender.time = this
    calender.set(Calendar.YEAR, calender.get(Calendar.YEAR))
    calender.set(Calendar.MONTH, calender.getActualMaximum(Calendar.MONTH))
    calender.set(Calendar.DAY_OF_MONTH, calender.getActualMaximum(Calendar.DAY_OF_MONTH))
    calender.set(Calendar.HOUR_OF_DAY, calender.getActualMaximum(Calendar.HOUR_OF_DAY))
    calender.set(Calendar.MINUTE, calender.getActualMaximum(Calendar.MINUTE))
    calender.set(Calendar.SECOND, calender.getActualMaximum(Calendar.SECOND))
    calender.set(Calendar.MILLISECOND, calender.getActualMaximum(Calendar.MILLISECOND))
    return calender.time
}

fun Date.add(field: Int, amount: Int): Date {
    val calender = this.calender
    calender.add(field, amount)
    return calender.time
}

/**
 * 獲取當前時間的下個月的Long值(日曆部分取得每個月第一天的List使用)
 * @author Hsieh
 * @date  2019/05/02
 * @param time 想知道下個月的long值的時間
 */
fun Date.getNextMonth(): Date {
    val calendar = calenderInst
    calendar.time = this
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.add(Calendar.MONTH, 1)
    return calendar.time
}

/**
 * 獲取當前時間所在年的週數
 * (此專案中主要用於取超過本年份的週數，例如說12/31可能為隔年第1周，此方法要取為前年的第53周。)
 * @author Hsieh
 * @date  2019/04/10
 * @param Date 想知道所在週數的時間
 */
val Date.weeksOfYear: Int
    get() {
        val calendar = calenderInst.apply { time = this@weeksOfYear }
        val yearStart = calenderInst.apply {
            clear()
            set(Calendar.YEAR, calendar.get(Calendar.YEAR))
        }
        var week = calendar.get(Calendar.WEEK_OF_YEAR)
        if (yearStart.timeInMillis > calendar.timeInMillis) {
            week = calenderInst.apply {
                clear()
                set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1)
                set(Calendar.MONTH, Calendar.DECEMBER)
                set(Calendar.DAY_OF_MONTH, 31)
            }.get(Calendar.WEEK_OF_YEAR)
        }
        return week
    }

/**
 * 取得傳入時間範圍的月份天數
 * @author Hsieh
 * @date 2020/05/27
 * @param startTime 取值開始時間
 * @param endTime 取值結束時間
 * */
fun Date.getDaysInRange(startTime: Long, endTime: Long): Int {
    val startDate = Date(startTime).getStartOfDay()
    val endDate = Date(endTime).getEndOfDay()
    return ((endDate.time + 1L - startDate.time) / TimeUnits.oneDay).toInt()
}

/**
 * 取得傳入時間範圍的月份週數
 * @author Hsieh
 * @date 2019/04/10
 * @param startTime 取值開始時間
 * @param endTime 取值結束時間
 * */
fun Date.getWeeksInRange(startTime: Long, endTime: Long): Int {
    val startDate = Date(startTime).getMonthFirstDay()
    val endDate = Date(endTime).getMonthEndDay()
    val startCal = calenderInst.apply { time = startDate }
    val endCal = calenderInst.apply { time = endDate }

    var startWeek = startCal.get(Calendar.WEEK_OF_YEAR)
    var endWeek = endCal.get(Calendar.WEEK_OF_YEAR)
    if (endWeek < startWeek) {
        // 如果發生換年週數問題，計算去年最後一天的週數
        val lastDayOfPrevYear = calenderInst.apply {
            set(Calendar.YEAR, startCal.get(Calendar.YEAR) - 1)
            set(Calendar.MONTH, Calendar.DECEMBER)
            set(Calendar.DAY_OF_MONTH, 31)
        }
        startWeek = lastDayOfPrevYear.get(Calendar.WEEK_OF_YEAR)
        endWeek += endCal.getActualMaximum(Calendar.WEEK_OF_YEAR)
    }
    return endWeek - startWeek + 1
}

/**
 * 時區轉換
 * @author wang
 * @date 2020/02/10
 * @param timeZone 指定時區
 * */
fun Date.toTimeZone(timeZone: TimeZone): Date {
    var date = Date(this.time + timeZone.rawOffset)
    if (timeZone.inDaylightTime(date)) {
        date = Date(date.time + timeZone.dstSavings)
    }
    return date
}

/**
 * 時區轉換
 * @author wang
 * @date 2020/02/10
 * @param from 從指定時區
 * @param to 轉換到指定時區
 * */
fun Date.toTimeZone(from: TimeZone, to: TimeZone): Date {
    return toTimeZone(from).toTimeZone(to)
}

/**
 * 時區轉換成+0
 * @author wang
 * @date 2020/02/10
 * @param timeZone date的時區
 * */
fun Date.toGMT(timeZone: TimeZone = TimeZone.getDefault()): Date {
    var date = Date(this.time + timeZone.rawOffset)
    if (timeZone.inDaylightTime(date)) {
        date = Date(date.time + timeZone.dstSavings)
    }
    return date
}

/**
 * 時區轉換成+0
 * @author wang
 * @date 2020/02/10
 * @param timeZoneId date的時區
 * */
fun Date.toGMT(timeZoneId: String): Date {
    return toGMT(TimeZone.getTimeZone(timeZoneId))
}

/**
 * 時區轉換成當地時區
 * @author wang
 * @date 2020/02/10
 * @param timeZone 要轉的時區
 * */
fun Date.toLocal(timeZone: TimeZone = TimeZone.getDefault()): Date {
    return toTimeZone(timeZone)
}