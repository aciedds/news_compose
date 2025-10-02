package com.example.news.utils

enum class NewsCountry(val value: String) {
    ARGENTINA("ar"),
    AUSTRALIA("au"),
    AUSTRIA("at"),
    BELGIUM("be"),
    BULGARIA("bg"),
    BRAZIL("br"),
    CANADA("ca"),
    SWITZERLAND("ch"),
    CHINA("cn"),
    COLOMBIA("co"),
    CUBA("cu"),
    CZECH("cz"),
    GERMANY("de"),
    EGYPT("eg"),
    FRANCE("fr"),
    UNITED_KINGDOM("gb"),
    GREECE("gr"),
    HONG_KONG("hk"),
    HUNGARY("hu"),
    INDONESIA("id"),
    IRELAND("ie"),
    ISRAEL("il"),
    INDIA("in"),
    ITALY("it"),
    JAPAN("jp"),
    SOUTH_KOREA("kr"),
    LATVIA("lv"),
    LITHUANIA("lt"),
    MEXICO("mx"),
    MALAYSIA("my"),
    NETHERLANDS("nl"),
    NORWAY("no"),
    NEW_ZEALAND("nz"),
    PANAMA("pa"),
    PERU("pe"),
    POLAND("pl"),
    PORTUGAL("pt"),
    ROMANIA("ro"),
    SINGAPORE("sg"),
    SLOVAKIA("sk"),
    SLOVENIA("si"),
    SOUTH_AFRICA("za"),
    TAIWAN("tw"),
    THAILAND("th"),
    TURKEY("tr"),
    UKRAINE("ua"),
    UNITED_STATES("us"),
    VENEZUELA("ve"),
    ZIMBABWE("zw");

    companion object {
        private val valueMap = entries.associateBy { it.value }
/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
        /**
         * Get a [NewsCountry] object from a given value.
         * This function is used to get the enum value from a string.
         * For example, if you have a string "id" and you want to get the
         * corresponding [NewsCountry] object, you can use this function.
         * @param value the string value of the enum
         * @return the corresponding [NewsCountry] object, or null if the value is not found
         */
/* <<<<<<<<<<  6909acf6-db2a-4004-ba3d-b8cdd9a25454  >>>>>>>>>>> */
        fun fromValue(value: String?): NewsCountry? = valueMap[value]
    }
}