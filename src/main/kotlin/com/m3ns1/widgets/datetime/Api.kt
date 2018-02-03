package com.m3ns1.widgets.datetime

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor


@RestController()
@RequestMapping("/api/v1/fmt/")
class WidgetsResource {

    @RequestMapping(value = "/", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun get(@RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) date: ZonedDateTime?,
            @RequestParam(value = "format", required = false, defaultValue = "yyyy-MM-dd'T'HH:mm:ss") format: String,
            @RequestParam(value = "timezone", required = false) timezone: String?): Widget {
        val widget = Widget(null, format)
        val targetTz = try {
            ZoneId.of(timezone)
        } catch (e: Exception) {
            null
        }
        val targetDate = if (targetTz != null) {
            (date ?: ZonedDateTime.now()).withZoneSameInstant(targetTz)
        } else {
            date ?: ZonedDateTime.now()
        }
        widget.result = build(targetDate, widget.format)
        return widget
    }

    private fun build(instant: TemporalAccessor, format: String): WidgetResult {
        return WidgetResult(value = DateTimeFormatter.ofPattern(format).format(instant))
    }
}


@RestController()
@RequestMapping("/api/v1/versions/")
class VersionsResource {

    @RequestMapping("/")
    fun version(): Version {
        return Version("1.0")
    }
}

@ControllerAdvice
class JsonpAdvice : AbstractJsonpResponseBodyAdvice("callback")

class Widget(
        var result: WidgetResult? = null,
        var format: String = "yyyy-MM-dd'T'HH:MM:ssZ"
)

data class WidgetResult(
        val value: String
)

data class Version(
        val name: String
)