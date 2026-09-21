package com.example.chatbot.data

import com.example.chatbot.model.KnowledgeCategory
import com.example.chatbot.model.KnowledgeChunk

/**
 * Authoritative Knowledge Base for Cheese Bites Restaurant.
 * Kept separate from UI and application logic to support dynamic updates (Part 9).
 */
object CheeseBiteKnowledgeBase {

    private val chunks = mutableListOf(
        // GENERAL INFORMATION
        KnowledgeChunk(
            id = "gen_ceo",
            title = "Restaurant Leadership & CEO",
            category = KnowledgeCategory.GENERAL_INFORMATION,
            content = "Cheese Bites Restaurant CEO is Subhani Javaid.",
            keywords = listOf("ceo", "owner", "founder", "subhani javaid", "subhani", "javaid", "leadership", "boss")
        ),
        KnowledgeChunk(
            id = "gen_announcement",
            title = "New Branch Announcement",
            category = KnowledgeCategory.ANNOUNCEMENTS,
            content = "Announcement: Insha Allah, in the next 2 months, a new branch will be opening!",
            keywords = listOf("announcement", "new branch", "upcoming branch", "expansion", "branch opening", "next 2 months")
        ),

        // LOCATION & GENERAL INFO
        KnowledgeChunk(
            id = "loc_address",
            title = "Restaurant Address & Location",
            category = KnowledgeCategory.LOCATION,
            content = "Address: Mandi Throo, Near Hamza Traders, Zafarwal Road.",
            keywords = listOf("address", "location", "where", "mandi throo", "hamza traders", "zafarwal road", "locate", "directions")
        ),
        KnowledgeChunk(
            id = "info_hours",
            title = "Opening Hours & Timings",
            category = KnowledgeCategory.OPENING_HOURS,
            content = "Opening Hours: 12:00 PM to 01:00 AM daily.",
            keywords = listOf("hours", "timings", "opening hours", "closing time", "open", "close", "time", "12 pm", "1 am")
        ),
        KnowledgeChunk(
            id = "info_services",
            title = "Services & Home Delivery",
            category = KnowledgeCategory.SERVICES,
            content = "Services: Free Home Delivery is provided for our valued customers.",
            keywords = listOf("services", "home delivery", "free delivery", "delivery charges", "delivery service", "deliver", "free home delivery")
        ),
        KnowledgeChunk(
            id = "info_contact",
            title = "Contact Numbers & Phone",
            category = KnowledgeCategory.CONTACT,
            content = "Contact Numbers: 0320-9163877 and 0306-7526655.",
            keywords = listOf("contact", "phone", "number", "call", "whatsapp", "mobile", "0320-9163877", "0306-7526655")
        ),

        // MENU ITEMS - CHEESE STICK / PASTA
        KnowledgeChunk(
            id = "menu_cheese_stick_pasta",
            title = "Cheese Stick & Pasta Menu",
            category = KnowledgeCategory.MENU,
            content = """
                Cheese Stick / Pasta:
                - Cheese Stick — Rs. 600
                - Cheese Panni Pasta — Rs. 700
                - Al Frado Pasta — Rs. 850
            """.trimIndent(),
            keywords = listOf("cheese stick", "pasta", "cheese panni pasta", "al frado pasta", "alfredo")
        ),

        // MENU ITEMS - SWEETS
        KnowledgeChunk(
            id = "menu_sweets",
            title = "Sweets Menu",
            category = KnowledgeCategory.MENU,
            content = """
                Sweets:
                - Cheese Kanafa — Rs. 850
                - Russian Salad — Rs. 300
            """.trimIndent(),
            keywords = listOf("sweets", "dessert", "cheese kanafa", "kunafa", "kanafa", "russian salad", "salad")
        ),

        // MENU ITEMS - DRINKS
        KnowledgeChunk(
            id = "menu_drinks",
            title = "Drinks & Beverages Menu",
            category = KnowledgeCategory.MENU,
            content = """
                Drinks:
                - Small Water — Rs. 60
                - Large Water — Rs. 100
                - Half Lrt Drink — Rs. 120
                - N.R Drink — Rs. 80
                - 1 Ltr. Drink — Rs. 180
                - 1.5 Ltr. Drink — Rs. 220
                - Drink Can — Rs. 130
            """.trimIndent(),
            keywords = listOf("drinks", "beverages", "water", "small water", "large water", "half ltr drink", "n.r drink", "nr drink", "1 ltr drink", "1.5 ltr drink", "drink can", "cold drink", "soda")
        ),

        // MENU ITEMS - FRIES
        KnowledgeChunk(
            id = "menu_fries",
            title = "Fries Menu",
            category = KnowledgeCategory.MENU,
            content = """
                Fries:
                - Regular Fries — Rs. 200
                - Family Fries — Rs. 270
                - Masala Fries — Rs. 250
                - Loaded Fries Small — Rs. 300
                - Loaded Fries Large — Rs. 550
            """.trimIndent(),
            keywords = listOf("fries", "regular fries", "family fries", "masala fries", "loaded fries", "loaded fries small", "loaded fries large", "chips")
        ),

        // MENU ITEMS - HOT WINGS & NUGGETS
        KnowledgeChunk(
            id = "menu_wings_nuggets",
            title = "Hot Wings & Nuggets Menu",
            category = KnowledgeCategory.MENU,
            content = """
                Hot Wings & Nuggets:
                - Hot Wings (10 Pc) — Rs. 600
                - Nugetts (10 Pc) — Rs. 500
            """.trimIndent(),
            keywords = listOf("hot wings", "wings", "nuggets", "nugetts", "10 pc wings", "10 pc nuggets")
        ),

        // MENU ITEMS - BAR.B.Q (COMING SOON)
        KnowledgeChunk(
            id = "menu_bbq",
            title = "Bar.B.Q Menu (Coming Soon)",
            category = KnowledgeCategory.MENU,
            content = """
                Bar.B.Q — Coming Soon:
                - Chicken Tika — Rs. 150
                - Chicken Kabab — Rs. 150
                - Chicken Malai Boti — Rs. 300
                - Chicken Green Boti — Rs. 350
                - Beef Kabab — Rs. 200
            """.trimIndent(),
            keywords = listOf("bbq", "bar.b.q", "barbeque", "barbecue", "chicken tika", "tikka", "chicken kabab", "chicken malai boti", "chicken green boti", "beef kabab", "coming soon")
        ),

        // MENU ITEMS - BURGERS
        KnowledgeChunk(
            id = "menu_burgers",
            title = "Burgers Menu",
            category = KnowledgeCategory.MENU,
            content = """
                Burgers:
                - Zinger Burger — Rs. 320
                - Chicken Petty Burger — Rs. 270
                - Double Doser — Rs. 450
                - Grill Burger — Rs. 450
                - Sizzelar Burger — Rs. 390
                - Tower Burger — Rs. 550
                - Pizza Burger — Rs. 500
            """.trimIndent(),
            keywords = listOf("burger", "burgers", "zinger burger", "zinger", "chicken petty burger", "petty burger", "patty burger", "double doser", "grill burger", "sizzelar burger", "sizzler burger", "tower burger", "pizza burger")
        ),

        // MENU ITEMS - WRAPS
        KnowledgeChunk(
            id = "menu_wraps",
            title = "Wraps Menu",
            category = KnowledgeCategory.MENU,
            content = """
                Wraps:
                - Tikka Wrap — Rs. 500
                - Fajita Wrap — Rs. 500
                - Special Wrap — Rs. 550
                - Zinger Wrap — Rs. 550
            """.trimIndent(),
            keywords = listOf("wrap", "wraps", "tikka wrap", "fajita wrap", "special wrap", "zinger wrap")
        ),

        // MENU ITEMS - SHAWARMA
        KnowledgeChunk(
            id = "menu_shawarma",
            title = "Shawarma Menu",
            category = KnowledgeCategory.MENU,
            content = """
                Shawarma:
                - Chicken Shawarma — Rs. 200
                - Zinger Shawarma — Rs. 300
                - Open Shawarma — Rs. 450
            """.trimIndent(),
            keywords = listOf("shawarma", "shwarma", "chicken shawarma", "zinger shawarma", "open shawarma")
        ),

        // PIZZAS - SIZES & POLICY
        KnowledgeChunk(
            id = "pizza_sizes_policy",
            title = "Pizza Sizes & Policy",
            category = KnowledgeCategory.PIZZAS,
            content = """
                Pizza Sizes:
                - Pan 8"
                - Small 10"
                - Med. 12" (Medium)
                - Lar. 14" (Large)
                - XL 16" (Extra Large)

                Important Policy: Doner Pizza is not added in any deal.
            """.trimIndent(),
            keywords = listOf("pizza sizes", "sizes", "pan 8", "small 10", "med 12", "medium", "lar 14", "large", "xl 16", "extra large", "doner pizza deal policy")
        ),

        // PIZZAS - SPECIAL PIZZA
        KnowledgeChunk(
            id = "pizza_special",
            title = "Special Pizza Menu & Prices",
            category = KnowledgeCategory.PIZZAS,
            content = """
                Special Pizzas (Pan 8" | Small 10" | Med. 12" | Lar. 14" | XL 16"):
                - Special Bite Pizza: Pan Rs. 650 | Small Rs. 1150 | Med Rs. 1450 | Lar Rs. 1850 | XL Rs. 2090
                - Doner Pizza: Pan Rs. 700 | Small Rs. 1200 | Med Rs. 1500 | Lar Rs. 1900 | XL Rs. 2200 (Note: Doner Pizza is not added in any deal)
                - Malai Pizza: Pan Rs. 650 | Small Rs. 1150 | Med Rs. 1450 | Lar Rs. 1850 | XL Rs. 2090
                - Kabab Pizza: Pan Rs. 650 | Small Rs. 1150 | Med Rs. 1450 | Lar Rs. 1850 | XL Rs. 2090
                - Crown Crust: Pan Rs. 650 | Small Rs. 1150 | Med Rs. 1450 | Lar Rs. 1850 | XL Rs. 2090
                - Lazaniya Pizza: Pan Rs. 650 | Small Rs. 1150 | Med Rs. 1450 | Lar Rs. 1850 | XL Rs. 2090
                - Biyari Kabab Pizza: Pan Rs. 650 | Small Rs. 1150 | Med Rs. 1450 | Lar Rs. 1850 | XL Rs. 2090
            """.trimIndent(),
            keywords = listOf("special pizza", "special bite pizza", "special bite", "doner pizza", "malai pizza", "kabab pizza", "crown crust", "lazaniya pizza", "lasagna pizza", "biyari kabab pizza", "bihari kabab pizza", "large special bite pizza", "pizza prices")
        ),

        // PIZZAS - REGULAR PIZZA
        KnowledgeChunk(
            id = "pizza_regular",
            title = "Regular Pizza Menu & Prices",
            category = KnowledgeCategory.PIZZAS,
            content = """
                Regular Pizzas (Pan 8" | Small 10" | Med. 12" | Lar. 14" | XL 16"):
                Sizes and Rates for all Regular Pizzas:
                Pan: Rs. 580 | Small: Rs. 990 | Med: Rs. 1280 | Lar: Rs. 1690 | XL: Rs. 1990

                Flavors:
                - Tikka: Pan Rs. 580 | Small Rs. 990 | Med Rs. 1280 | Lar Rs. 1690 | XL Rs. 1990
                - Fajita: Pan Rs. 580 | Small Rs. 990 | Med Rs. 1280 | Lar Rs. 1690 | XL Rs. 1990
                - Super Spreem: Pan Rs. 580 | Small Rs. 990 | Med Rs. 1280 | Lar Rs. 1690 | XL Rs. 1990
                - Vegi Lover: Pan Rs. 580 | Small Rs. 990 | Med Rs. 1280 | Lar Rs. 1690 | XL Rs. 1990
                - Cheese Lover: Pan Rs. 580 | Small Rs. 990 | Med Rs. 1280 | Lar Rs. 1690 | XL Rs. 1990
                - Mexican: Pan Rs. 580 | Small Rs. 990 | Med Rs. 1280 | Lar Rs. 1690 | XL Rs. 1990
            """.trimIndent(),
            keywords = listOf("regular pizza", "tikka pizza", "fajita pizza", "super spreem", "supreme", "vegi lover", "veggie lover", "cheese lover", "mexican pizza", "pizza price")
        ),

        // DEALS - STUDENT DEALS
        KnowledgeChunk(
            id = "deal_student",
            title = "Student Deals",
            category = KnowledgeCategory.DEALS,
            content = """
                Student Deals:
                - Student Deal 1 — Rs. 1750: 1 Pan Pizza, 2 Zinger Burgers, 5 Nuggets, 5 Hot Wings, 1 Ltr. Drink
                - Student Deal 2 — Rs. 2000: 1 Small Pizza, 2 Zinger Burgers, 5 Nuggets, 1 Regular Fries, 1 Ltr. Drink
                - Student Deal 3 — Rs. 1700: 1 Med. Pizza, 5 Nuggets, 1 Reg. Fries, 1 Ltr. Drink
            """.trimIndent(),
            keywords = listOf("student deals", "student deal", "student deal 1", "student deal 2", "student deal 3", "student package")
        ),

        // DEALS - PIZZA DEALS
        KnowledgeChunk(
            id = "deal_pizza",
            title = "Pizza Deals (Deals 1 to 10)",
            category = KnowledgeCategory.DEALS,
            content = """
                Pizza Deals:
                - Deal 01 — Rs. 4000: 2 XL Pizza, 1.5 Ltr. Drink
                - Deal 02 — Rs. 2800: 1 XL Pizza, 1 Regular Fries, 5 Hot Wings, 5 Nuggets, 1.5 Ltr. Drink
                - Deal 03 — Rs. 3400: 2 Large Pizza, 1.5 Ltr. Drink
                - Deal 04 — Rs. 2050: 1 Large Pizza, 5 Hot Wings, 1 Ltr. Drink
                - Deal 05 — Rs. 1600: 1 Med. Pizza, 5 Hot Wings, 1 Ltr. Drink
                - Deal 06 — Rs. 1550: 1 Med. Pizza, 1 Reg. Fries, 1 Ltr. Drink
                - Deal 07 — Rs. 2000: 2 Small Pizza, 1 Ltr. Drink
                - Deal 08 — Rs. 1350: 1 Small Pizza, 1 Regular Fries, 1 0.5 Ltr. Drink
                - Deal 09 — Rs. 800: 1 Pan Pizza, 1 Regular Fries, 1 N.R Drink
                - Deal 10 — Rs. 3500: 1 Large Pizza, 5 Zinger Burger, 2 1.5 Ltr. Drink
            """.trimIndent(),
            keywords = listOf("pizza deals", "pizza deal", "deal 01", "deal 02", "deal 03", "deal 04", "deal 05", "deal 06", "deal 07", "deal 08", "deal 09", "deal 10", "deal 1", "deal 2", "deal 3", "deal 4", "deal 5", "deal 6", "deal 7", "deal 8", "deal 9", "large pizza deal", "xl pizza deal")
        ),

        // DEALS - BURGER DEALS
        KnowledgeChunk(
            id = "deal_burger",
            title = "Burger Deals (Deals 1 to 6)",
            category = KnowledgeCategory.DEALS,
            content = """
                Burger Deals:
                - Deal 01 — Rs. 1000: 2 Zinger Burger, 5 Hot Wings, 1 Ltr. Drink
                - Deal 02 — Rs. 1250: 3 Zinger Burger, 1 Regular Fries, 1 Ltr. Drink
                - Deal 03 — Rs. 1900: 5 Zinger Burger, 5 Hot Wings, 1 1.5 Ltr. Drink
                - Deal 04 — Rs. 1450: 5 Chicken Petty Burger, 1 1.5 Ltr. Drink
                - Deal 05 — Rs. 2000: 1 Medium Pizza, 2 Zinger Burger, 1 Ltr. Drink
                - Deal 06 — Rs. 1750: 5 Chicken Shawarma, 2 Zinger Burger, 1 1.5 Ltr. Drink
            """.trimIndent(),
            keywords = listOf("burger deals", "burger deal", "burger deal 1", "burger deal 2", "burger deal 3", "burger deal 4", "burger deal 5", "burger deal 6", "burger deal 01", "burger deal 02", "burger deal 03", "burger deal 04", "burger deal 05", "burger deal 06", "zinger deal")
        )
    )

    /**
     * Get all chunks in the knowledge base.
     */
    fun getAllChunks(): List<KnowledgeChunk> = synchronized(chunks) {
        chunks.toList()
    }

    /**
     * Get chunks by specific category.
     */
    fun getChunksByCategory(category: KnowledgeCategory): List<KnowledgeChunk> = synchronized(chunks) {
        chunks.filter { it.category == category }
    }

    /**
     * Find chunk by ID.
     */
    fun getChunkById(id: String): KnowledgeChunk? = synchronized(chunks) {
        chunks.firstOrNull { it.id == id }
    }

    /**
     * Update an existing chunk or add new chunk dynamically without rebuilding (Part 9).
     */
    fun updateOrAddChunk(chunk: KnowledgeChunk) = synchronized(chunks) {
        val index = chunks.indexOfFirst { it.id == chunk.id }
        if (index >= 0) {
            chunks[index] = chunk
        } else {
            chunks.add(chunk)
        }
    }

    /**
     * Update content of a specific chunk.
     */
    fun updateChunkContent(chunkId: String, newContent: String): Boolean = synchronized(chunks) {
        val index = chunks.indexOfFirst { it.id == chunkId }
        if (index >= 0) {
            chunks[index] = chunks[index].copy(content = newContent)
            true
        } else {
            false
        }
    }
}
