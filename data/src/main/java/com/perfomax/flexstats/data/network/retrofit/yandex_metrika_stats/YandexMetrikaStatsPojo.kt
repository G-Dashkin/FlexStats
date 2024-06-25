package com.perfomax.flexstats.data.network.retrofit.yandex_metrika_stats

data class YandexMetrikaStatsPojo(
    val contains_sensitive_data: Boolean,
    val `data`: List<Data>,
    val data_lag: Int,
    val max: List<Double>,
    val min: List<Double>,
    val query: Query,
    val sample_share: Double,
    val sample_size: Int,
    val sample_space: Int,
    val sampled: Boolean,
    val total_rows: Int,
    val total_rows_rounded: Boolean,
    val totals: List<Double>
) {
    data class Data(
        val dimensions: List<Dimension>,
        val metrics: List<Double>
    ) {
        data class Dimension(
            val name: String
        )
    }

    data class Query(
        val adfox_event_id: String,
        val attr_name: String,
        val attribution: String,
        val auto_group_size: String,
        val currency: String,
        val date1: String,
        val date2: String,
        val dimensions: List<String>,
        val funnel_pattern: String,
        val funnel_window: String,
        val group: String,
        val ids: List<Int>,
        val limit: Int,
        val metrics: List<String>,
        val offline_window: String,
        val offset: Int,
        val quantile: String,
        val sort: List<String>
    )
}