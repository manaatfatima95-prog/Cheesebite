package com.example.data

import com.example.R
import com.example.model.AppNotification
import com.example.model.DeliveryAddress
import com.example.model.FoodAddOn
import com.example.model.FoodCategory
import com.example.model.FoodItem
import com.example.model.FoodSize
import com.example.model.UserProfile

object CheeseBiteRepository {

    val defaultAddOns = listOf(
        FoodAddOn("addon_cheese", "Extra Melted Cheese", 150.0),
        FoodAddOn("addon_dip", "Garlic Mayo Sauce Dip", 60.0),
        FoodAddOn("addon_jalapeno", "Spicy Jalapeno Slices", 50.0),
        FoodAddOn("addon_drink", "Chilled Drink Can", 130.0)
    )

    private val specialPizzaSizes = listOf(
        FoodSize("Pan 8\"", 650.0),
        FoodSize("Small 10\"", 1150.0),
        FoodSize("Med 12\"", 1450.0),
        FoodSize("Large 14\"", 1850.0),
        FoodSize("XL 16\"", 2090.0)
    )

    private val donerPizzaSizes = listOf(
        FoodSize("Pan 8\"", 700.0),
        FoodSize("Small 10\"", 1200.0),
        FoodSize("Med 12\"", 1500.0),
        FoodSize("Large 14\"", 1900.0),
        FoodSize("XL 16\"", 2200.0)
    )

    private val regularPizzaSizes = listOf(
        FoodSize("Pan 8\"", 580.0),
        FoodSize("Small 10\"", 990.0),
        FoodSize("Med 12\"", 1280.0),
        FoodSize("Large 14\"", 1690.0),
        FoodSize("XL 16\"", 1990.0)
    )

    val categories = listOf(
        FoodCategory("deals", "Deals & Combos", "🔥", "Mega savings on pizza & burger combos"),
        FoodCategory("special_pizza", "Special Pizza", "🍕", "Gourmet crust with loaded premium toppings"),
        FoodCategory("regular_pizza", "Regular Pizza", "🧀", "Classic favorites baked fresh to perfection"),
        FoodCategory("burgers", "Burgers", "🍔", "Crispy zingers and juicy grilled burgers"),
        FoodCategory("wraps", "Wraps", "🌯", "Fresh grilled chicken rolled in soft tortilla"),
        FoodCategory("shawarma", "Shawarma", "🥙", "Authentic spiced chicken & creamy garlic tahini"),
        FoodCategory("cheese_pasta", "Cheese Stick & Pasta", "🍝", "Al-Frado, cheese sticks & baked panni pasta"),
        FoodCategory("fries", "Fries & Sides", "🍟", "Loaded cheese fries & seasoned crispy potatoes"),
        FoodCategory("hot_wings", "Wings & Nuggets", "🍗", "Golden crispy wings and tenders"),
        FoodCategory("bbq", "Bar. B.Q", "🍢", "Charcoal-grilled tikka & boti skewers"),
        FoodCategory("sweets", "Sweets & Desserts", "🍰", "Cheese kanafa & fresh sweet salad"),
        FoodCategory("drinks", "Chilled Drinks", "🥤", "Water bottles, soft drinks & cold cans")
    )

    val foodItems: List<FoodItem> = listOf(
        // === PIZZA DEALS ===
        FoodItem(
            id = "deal_pizza_01",
            name = "Pizza Deal 01",
            description = "2 XL Pizzas (16\") + 1.5 Ltr Chilled Cold Drink. Perfect for grand family feasts.",
            category = "deals",
            basePrice = 4000.0,
            imageRes = R.drawable.food_special_pizza,
            isBestseller = true,
            rating = 4.9,
            reviewCount = 89
        ),
        FoodItem(
            id = "deal_pizza_02",
            name = "Pizza Deal 02",
            description = "1 XL Pizza + 1 Regular Fries + 5 Hot Wings + 5 Nuggets + 1.5 Ltr Cold Drink.",
            category = "deals",
            basePrice = 2800.0,
            imageRes = R.drawable.banner_promo_deal,
            isBestseller = true,
            rating = 4.9,
            reviewCount = 112
        ),
        FoodItem(
            id = "deal_pizza_03",
            name = "Pizza Deal 03",
            description = "2 Large Pizzas (14\") + 1.5 Ltr Cold Drink. Huge saving combo!",
            category = "deals",
            basePrice = 3400.0,
            imageRes = R.drawable.food_special_pizza,
            rating = 4.8
        ),
        FoodItem(
            id = "deal_pizza_04",
            name = "Pizza Deal 04",
            description = "1 Large Pizza + 5 Hot Wings + 1 Ltr Cold Drink.",
            category = "deals",
            basePrice = 2050.0,
            imageRes = R.drawable.food_special_pizza,
            rating = 4.7
        ),
        FoodItem(
            id = "deal_pizza_05",
            name = "Pizza Deal 05",
            description = "1 Medium Pizza (12\") + 5 Hot Wings + 1 Ltr Cold Drink.",
            category = "deals",
            basePrice = 1600.0,
            imageRes = R.drawable.food_special_pizza,
            isBestseller = true,
            rating = 4.9,
            reviewCount = 76
        ),
        FoodItem(
            id = "deal_pizza_06",
            name = "Pizza Deal 06",
            description = "1 Medium Pizza (12\") + 1 Regular Fries + 1 Ltr Cold Drink.",
            category = "deals",
            basePrice = 1550.0,
            imageRes = R.drawable.food_loaded_fries,
            rating = 4.8
        ),
        FoodItem(
            id = "deal_pizza_07",
            name = "Pizza Deal 07",
            description = "2 Small Pizzas (10\") + 1 Ltr Cold Drink. Ideal for pairs.",
            category = "deals",
            basePrice = 2000.0,
            imageRes = R.drawable.food_special_pizza,
            rating = 4.8
        ),
        FoodItem(
            id = "deal_pizza_08",
            name = "Pizza Deal 08",
            description = "1 Small Pizza + 1 Regular Fries + 0.5 Ltr Drink.",
            category = "deals",
            basePrice = 1350.0,
            imageRes = R.drawable.food_special_pizza,
            rating = 4.7
        ),
        FoodItem(
            id = "deal_pizza_09",
            name = "Pizza Deal 09 (Single Delight)",
            description = "1 Pan Pizza (8\") + 1 Regular Fries + 1 N.R Cold Drink.",
            category = "deals",
            basePrice = 800.0,
            imageRes = R.drawable.food_special_pizza,
            rating = 4.8,
            isBestseller = true
        ),
        FoodItem(
            id = "deal_pizza_10",
            name = "Pizza Deal 10 (Mega Party)",
            description = "1 Large Pizza + 5 Zinger Burgers + Two 1.5 Ltr Cold Drinks.",
            category = "deals",
            basePrice = 3500.0,
            imageRes = R.drawable.banner_promo_deal,
            rating = 4.9,
            reviewCount = 94
        ),

        // === BURGER DEALS ===
        FoodItem(
            id = "deal_burger_01",
            name = "Burger Deal 01",
            description = "2 Zinger Burgers + 5 Hot Wings + 1 Ltr Cold Drink.",
            category = "deals",
            basePrice = 1000.0,
            imageRes = R.drawable.food_zinger_burger,
            isBestseller = true,
            rating = 4.9,
            reviewCount = 130
        ),
        FoodItem(
            id = "deal_burger_02",
            name = "Burger Deal 02",
            description = "3 Zinger Burgers + 1 Regular Fries + 1 Ltr Cold Drink.",
            category = "deals",
            basePrice = 1250.0,
            imageRes = R.drawable.food_zinger_burger,
            rating = 4.8
        ),
        FoodItem(
            id = "deal_burger_03",
            name = "Burger Deal 03 (Feast)",
            description = "5 Zinger Burgers + 5 Hot Wings + 1 1.5 Ltr Cold Drink.",
            category = "deals",
            basePrice = 1900.0,
            imageRes = R.drawable.food_zinger_burger,
            isBestseller = true,
            rating = 4.9
        ),
        FoodItem(
            id = "deal_burger_04",
            name = "Burger Deal 04 (Petty Pack)",
            description = "5 Chicken Petty Burgers + 1 1.5 Ltr Cold Drink.",
            category = "deals",
            basePrice = 1450.0,
            imageRes = R.drawable.food_zinger_burger,
            rating = 4.7
        ),
        FoodItem(
            id = "deal_burger_05",
            name = "Burger Deal 05 (Combo Master)",
            description = "1 Medium Pizza + 2 Zinger Burgers + 1 Ltr Cold Drink.",
            category = "deals",
            basePrice = 2000.0,
            imageRes = R.drawable.banner_promo_deal,
            isBestseller = true,
            rating = 4.9
        ),
        FoodItem(
            id = "deal_burger_06",
            name = "Burger Deal 06 (Shawarma Combo)",
            description = "5 Chicken Shawarmas + 2 Zinger Burgers + 1 1.5 Ltr Cold Drink.",
            category = "deals",
            basePrice = 1750.0,
            imageRes = R.drawable.food_zinger_burger,
            rating = 4.8
        ),

        // === STUDENTS DEALS ===
        FoodItem(
            id = "deal_student_01",
            name = "Student Deal 1",
            description = "1 Pan Pizza + 2 Zinger Burgers + 5 Nuggets + 5 Hot Wings + 1 Ltr Cold Drink.",
            category = "deals",
            basePrice = 1750.0,
            imageRes = R.drawable.banner_promo_deal,
            isBestseller = true,
            rating = 4.9
        ),
        FoodItem(
            id = "deal_student_02",
            name = "Student Deal 2",
            description = "1 Small Pizza + 2 Zinger Burgers + 5 Nuggets + 1 Regular Fries + 1 Ltr Drink.",
            category = "deals",
            basePrice = 2000.0,
            imageRes = R.drawable.banner_promo_deal,
            rating = 4.8
        ),
        FoodItem(
            id = "deal_student_03",
            name = "Student Deal 3",
            description = "1 Med Pizza + 5 Nuggets + 1 Regular Fries + 1 Ltr Drink.",
            category = "deals",
            basePrice = 1700.0,
            imageRes = R.drawable.banner_promo_deal,
            rating = 4.8
        ),

        // === SPECIAL PIZZA ===
        FoodItem(
            id = "sp_cheese_bite",
            name = "Special Bite Pizza",
            description = "Signature house pizza with melted mozzarella, seasoned chicken chunks, olives, bell pepper, and chef secret cheese sauce.",
            category = "special_pizza",
            basePrice = 650.0,
            sizes = specialPizzaSizes,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_special_pizza,
            isBestseller = true,
            rating = 4.9,
            reviewCount = 145
        ),
        FoodItem(
            id = "sp_doner",
            name = "Doner Pizza",
            description = "Succulent Turkish doner meat slices, secret herbs, red onions, garlic sauce drizzle, and golden cheese.",
            category = "special_pizza",
            basePrice = 700.0,
            sizes = donerPizzaSizes,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_special_pizza,
            rating = 4.8
        ),
        FoodItem(
            id = "sp_malai",
            name = "Malai Pizza",
            description = "Creamy malai boti chicken chunks, rich white sauce, mild spices, and thick layer of melted mozzarella.",
            category = "special_pizza",
            basePrice = 650.0,
            sizes = specialPizzaSizes,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_special_pizza,
            rating = 4.8,
            isBestseller = true
        ),
        FoodItem(
            id = "sp_kabab",
            name = "Kabab Pizza",
            description = "Charcoal spiced Seekh Kabab slices arranged around the crust with tangy tomato base and fresh vegetables.",
            category = "special_pizza",
            basePrice = 650.0,
            sizes = specialPizzaSizes,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_special_pizza,
            rating = 4.7
        ),
        FoodItem(
            id = "sp_crown_crust",
            name = "Crown Crust Pizza",
            description = "Distinctive regal crown stuffed crust filled with spiced chicken balls and creamy melted cheese.",
            category = "special_pizza",
            basePrice = 650.0,
            sizes = specialPizzaSizes,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_special_pizza,
            isBestseller = true,
            rating = 4.9
        ),
        FoodItem(
            id = "sp_lazaniya",
            name = "Lazaniya Pizza",
            description = "Layered meat sauce, Italian seasonings, lasagna noodles texture, and double melted cheddar cheese.",
            category = "special_pizza",
            basePrice = 650.0,
            sizes = specialPizzaSizes,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_special_pizza,
            rating = 4.7
        ),
        FoodItem(
            id = "sp_biyari_kabab",
            name = "Biyari Kabab Pizza",
            description = "Authentic Bihari spicy beef/chicken kabab topping with onions, green chilies, and coriander zest.",
            category = "special_pizza",
            basePrice = 650.0,
            sizes = specialPizzaSizes,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_special_pizza,
            rating = 4.8
        ),

        // === REGULAR PIZZA ===
        FoodItem(
            id = "reg_tikka",
            name = "Chicken Tikka Pizza",
            description = "Traditional Pakistani chicken tikka, red onion rings, green capsicum, and golden mozzarella cheese.",
            category = "regular_pizza",
            basePrice = 580.0,
            sizes = regularPizzaSizes,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_special_pizza,
            isBestseller = true,
            rating = 4.8
        ),
        FoodItem(
            id = "reg_fajita",
            name = "Chicken Fajita Pizza",
            description = "Mexican marinated fajita chicken with bell peppers, sliced black olives, and savory herbs.",
            category = "regular_pizza",
            basePrice = 580.0,
            sizes = regularPizzaSizes,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_special_pizza,
            rating = 4.7
        ),
        FoodItem(
            id = "reg_super_spreem",
            name = "Super Spreem Pizza",
            description = "Supreme loaded combination of chicken chunks, pepperoni slices, mushrooms, onions, and olives.",
            category = "regular_pizza",
            basePrice = 580.0,
            sizes = regularPizzaSizes,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_special_pizza,
            rating = 4.8
        ),
        FoodItem(
            id = "reg_vegi_lover",
            name = "Vegi Lover Pizza",
            description = "Crisp bell peppers, fresh mushrooms, tomatoes, sweet corn, black olives, and herbs on mozzarella.",
            category = "regular_pizza",
            basePrice = 580.0,
            sizes = regularPizzaSizes,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_special_pizza,
            rating = 4.6
        ),
        FoodItem(
            id = "reg_cheese_lover",
            name = "Cheese Lover Pizza",
            description = "Loaded double crust with rich golden mozzarella, cheddar cheese sauce, and oregano sprinkles.",
            category = "regular_pizza",
            basePrice = 580.0,
            sizes = regularPizzaSizes,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_special_pizza,
            isBestseller = true,
            rating = 4.9
        ),
        FoodItem(
            id = "reg_mexican",
            name = "Mexican Pizza",
            description = "Zesty jalapeno slices, spicy Mexican salsa sauce, seasoned ground chicken, and bubbling cheese.",
            category = "regular_pizza",
            basePrice = 580.0,
            sizes = regularPizzaSizes,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_special_pizza,
            rating = 4.7
        ),

        // === BURGERS ===
        FoodItem(
            id = "bur_zinger",
            name = "Zinger Burger",
            description = "Crispy golden fried whole chicken thigh fillet, fresh iceberg lettuce, creamy garlic mayo, toasted bun.",
            category = "burgers",
            basePrice = 320.0,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_zinger_burger,
            isBestseller = true,
            rating = 4.9,
            reviewCount = 205
        ),
        FoodItem(
            id = "bur_chicken_petty",
            name = "Chicken Petty Burger",
            description = "Tender spiced minced chicken patty grilled to perfection with signature burger sauce and crunchy onions.",
            category = "burgers",
            basePrice = 270.0,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_zinger_burger,
            rating = 4.6
        ),
        FoodItem(
            id = "bur_double_doser",
            name = "Double Doser Burger",
            description = "Double crunchy zinger fillets stacked with melted cheese slice and smoky barbecue glaze.",
            category = "burgers",
            basePrice = 450.0,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_zinger_burger,
            isBestseller = true,
            rating = 4.9
        ),
        FoodItem(
            id = "bur_grill",
            name = "Grill Burger",
            description = "Juicy charcoal-grilled boneless chicken breast marinated in herbs, topped with melted cheese and pickles.",
            category = "burgers",
            basePrice = 450.0,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_zinger_burger,
            rating = 4.8
        ),
        FoodItem(
            id = "bur_sizzelar",
            name = "Sizzelar Burger",
            description = "Spicy sizzling chicken fillet coated in fiery chili mayo with jalapeños and crispy onion strings.",
            category = "burgers",
            basePrice = 390.0,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_zinger_burger,
            rating = 4.7
        ),
        FoodItem(
            id = "bur_tower",
            name = "Tower Burger",
            description = "Massive stack with crunchy zinger fillet, crispy golden hash brown patty, double cheese, and sauce.",
            category = "burgers",
            basePrice = 550.0,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_zinger_burger,
            isBestseller = true,
            rating = 4.9
        ),
        FoodItem(
            id = "bur_pizza",
            name = "Pizza Buger",
            description = "Innovative fusion: burger bun stuffed with pizza toppings, pepperoni, melted mozzarella, and pizza sauce!",
            category = "burgers",
            basePrice = 500.0,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_zinger_burger,
            rating = 4.8
        ),

        // === WRAPS ===
        FoodItem(
            id = "wrap_tikka",
            name = "Tikka Wrap",
            description = "Grilled chicken tikka chunks wrapped in soft flatbread with mint chutney and crisp onions.",
            category = "wraps",
            basePrice = 500.0,
            availableAddOns = defaultAddOns,
            rating = 4.7
        ),
        FoodItem(
            id = "wrap_fajita",
            name = "Fajita Wrap",
            description = "Tender fajita strips with sautéed bell peppers, sweet corn, and sour cream dressing.",
            category = "wraps",
            basePrice = 500.0,
            availableAddOns = defaultAddOns,
            rating = 4.7
        ),
        FoodItem(
            id = "wrap_special",
            name = "Special Wrap",
            description = "Chef special combination of grilled boti and crispy strips with loaded cheese drizzle.",
            category = "wraps",
            basePrice = 550.0,
            availableAddOns = defaultAddOns,
            isBestseller = true,
            rating = 4.9
        ),
        FoodItem(
            id = "wrap_zinger",
            name = "Zinger Wrap",
            description = "Crispy zinger chicken strips, shredded lettuce, garlic mayo, and pickles rolled in warm tortilla.",
            category = "wraps",
            basePrice = 550.0,
            availableAddOns = defaultAddOns,
            rating = 4.8
        ),

        // === SHAWARMA ===
        FoodItem(
            id = "shw_chicken",
            name = "Chicken Shawarma",
            description = "Thinly shaved rotisserie chicken wrapped in pita bread with pickled cucumbers and garlic toum sauce.",
            category = "shawarma",
            basePrice = 200.0,
            availableAddOns = defaultAddOns,
            isBestseller = true,
            rating = 4.8
        ),
        FoodItem(
            id = "shw_zinger",
            name = "Zinger Shawarma",
            description = "Crunchy fried zinger chicken strips wrapped in shawarma bread with spicy mayo sauce.",
            category = "shawarma",
            basePrice = 300.0,
            availableAddOns = defaultAddOns,
            rating = 4.8
        ),
        FoodItem(
            id = "shw_open",
            name = "Open Shawarma Platter",
            description = "Generous bowl of shredded shawarma chicken served with cut pita pieces, fries, pickles, and dipping sauces.",
            category = "shawarma",
            basePrice = 450.0,
            availableAddOns = defaultAddOns,
            rating = 4.9
        ),

        // === CHEESE STICK & PASTA ===
        FoodItem(
            id = "cs_cheese_stick",
            name = "Cheese Stick (Crispy)",
            description = "Crispy breaded mozzarella cheese sticks deep fried till golden brown with warm marinara dipping sauce.",
            category = "cheese_pasta",
            basePrice = 600.0,
            availableAddOns = defaultAddOns,
            isBestseller = true,
            rating = 4.9
        ),
        FoodItem(
            id = "cs_panni_pasta",
            name = "Cheese Panni Pasta",
            description = "Penne pasta baked under a thick golden blanket of cheese with rich spiced chicken and creamy sauce.",
            category = "cheese_pasta",
            basePrice = 700.0,
            availableAddOns = defaultAddOns,
            rating = 4.8
        ),
        FoodItem(
            id = "cs_al_frado_pasta",
            name = "Al Frado Pasta",
            description = "Classic fettuccine pasta in rich buttery Parmesan Alfredo garlic sauce with grilled chicken breast.",
            category = "cheese_pasta",
            basePrice = 850.0,
            availableAddOns = defaultAddOns,
            isBestseller = true,
            rating = 4.9
        ),

        // === FRIES & SIDES ===
        FoodItem(
            id = "fries_regular",
            name = "Regular Fries",
            description = "Golden salted crispy potato fries served with tomato ketchup.",
            category = "fries",
            basePrice = 200.0,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_loaded_fries,
            rating = 4.6
        ),
        FoodItem(
            id = "fries_family",
            name = "Family Fries",
            description = "Extra-large jumbo basket of fresh crispy golden fries for the whole family.",
            category = "fries",
            basePrice = 270.0,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_loaded_fries,
            rating = 4.7
        ),
        FoodItem(
            id = "fries_masala",
            name = "Masala Fries",
            description = "Crispy fries tossed in aromatic spicy Pakistani chaat masala herbs.",
            category = "fries",
            basePrice = 250.0,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_loaded_fries,
            rating = 4.8
        ),
        FoodItem(
            id = "fries_loaded_small",
            name = "Loaded Fries (Small)",
            description = "Crispy fries topped with melted cheddar cheese sauce, chicken chunks, and jalapenos.",
            category = "fries",
            basePrice = 300.0,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_loaded_fries,
            isBestseller = true,
            rating = 4.9
        ),
        FoodItem(
            id = "fries_loaded_large",
            name = "Loaded Fries (Large)",
            description = "Heaping platter of crispy fries covered in bubbling melted cheese, shredded zinger, chipotle sauce, and jalapenos.",
            category = "fries",
            basePrice = 550.0,
            availableAddOns = defaultAddOns,
            imageRes = R.drawable.food_loaded_fries,
            isBestseller = true,
            rating = 4.9
        ),

        // === HOT WINGS & NUGGETS ===
        FoodItem(
            id = "wings_hot_10",
            name = "Hot Wings (10 Pc)",
            description = "10 pieces of fiery crispy fried chicken wings coated in secret spices.",
            category = "hot_wings",
            basePrice = 600.0,
            availableAddOns = defaultAddOns,
            isBestseller = true,
            rating = 4.9
        ),
        FoodItem(
            id = "nuggets_10",
            name = "Nugetts (10 Pc)",
            description = "10 pieces of bite-sized golden chicken nuggets served with honey mustard dip.",
            category = "hot_wings",
            basePrice = 500.0,
            availableAddOns = defaultAddOns,
            rating = 4.7
        ),

        // === BAR. B.Q (Coming Soon / Special) ===
        FoodItem(
            id = "bbq_chicken_tika",
            name = "Chicken Tika",
            description = "Succulent chicken quarter leg/breast marinated in traditional spices, char-grilled over hot coals.",
            category = "bbq",
            basePrice = 150.0,
            rating = 4.8
        ),
        FoodItem(
            id = "bbq_chicken_kabab",
            name = "Chicken Kabab",
            description = "Spiced minced chicken skewer grilled with onion and mint aroma.",
            category = "bbq",
            basePrice = 150.0,
            rating = 4.7
        ),
        FoodItem(
            id = "bbq_malai_boti",
            name = "Chicken Malai Boti",
            description = "Melt-in-your-mouth boneless chicken cubes marinated in heavy cream, yogurt, and white pepper.",
            category = "bbq",
            basePrice = 300.0,
            rating = 4.9
        ),
        FoodItem(
            id = "bbq_green_boti",
            name = "Chicken Green Boti",
            description = "Tender chicken marinated in crushed coriander, mint, green chilies, and lemon juice.",
            category = "bbq",
            basePrice = 350.0,
            rating = 4.8
        ),
        FoodItem(
            id = "bbq_beef_kabab",
            name = "Beef Kabab",
            description = "Juicy ground beef kababs seasoned with roasted cumin, coriander seeds, and crushed chili.",
            category = "bbq",
            basePrice = 200.0,
            rating = 4.8
        ),

        // === SWEETS ===
        FoodItem(
            id = "sweet_kanafa",
            name = "Cheese Kanafa",
            description = "Golden crunchy shredded phyllo pastry filled with gooey sweet stretchy cheese and soaked in rose water syrup.",
            category = "sweets",
            basePrice = 850.0,
            isBestseller = true,
            rating = 4.9
        ),
        FoodItem(
            id = "sweet_russian_salad",
            name = "Russian Salad",
            description = "Diced seasonal fruits, sweet peas, pineapple chunks, and potatoes in chilled sweet cream mayonnaise dressing.",
            category = "sweets",
            basePrice = 300.0,
            rating = 4.7
        ),

        // === DRINKS ===
        FoodItem(
            id = "drk_water_sm",
            name = "Small Mineral Water",
            description = "Purified 500ml drinking water bottle.",
            category = "drinks",
            basePrice = 60.0
        ),
        FoodItem(
            id = "drk_water_lg",
            name = "Large Mineral Water",
            description = "Purified 1.5 Liter drinking water bottle.",
            category = "drinks",
            basePrice = 100.0
        ),
        FoodItem(
            id = "drk_half_ltr",
            name = "Half Ltr Cold Drink",
            description = "Chilled 500ml bottle (Pepsi, 7Up, Mirinda, or Marinda).",
            category = "drinks",
            basePrice = 120.0
        ),
        FoodItem(
            id = "drk_nr",
            name = "N.R Cold Drink",
            description = "Single serving returnable bottle chilled soft drink.",
            category = "drinks",
            basePrice = 80.0
        ),
        FoodItem(
            id = "drk_1_ltr",
            name = "1 Ltr Cold Drink",
            description = "Chilled 1.0 Liter bottle of your favorite soda.",
            category = "drinks",
            basePrice = 180.0
        ),
        FoodItem(
            id = "drk_1_5_ltr",
            name = "1.5 Ltr Cold Drink",
            description = "Chilled 1.5 Liter jumbo bottle, ideal for deals and combos.",
            category = "drinks",
            basePrice = 220.0
        ),
        FoodItem(
            id = "drk_can",
            name = "Drink Can (250ml)",
            description = "Chilled aluminium soda can (Pepsi, 7Up, Dew, Coca-Cola).",
            category = "drinks",
            basePrice = 130.0
        )
    )

    val defaultAddresses = listOf(
        DeliveryAddress(
            id = "addr_1",
            label = "Home",
            fullAddress = "House 14, Street 2, Mandi Throo",
            landmark = "Near Hamza Traders, Zafarwal Road",
            phone = "0306-7526655",
            isDefault = true
        ),
        DeliveryAddress(
            id = "addr_2",
            label = "Office / Shop",
            fullAddress = "Shop 5, Main Commercial Market, Zafarwal Road",
            landmark = "Opposite Allied Bank",
            phone = "0320-9163877",
            isDefault = false
        )
    )

    val initialNotifications = listOf(
        AppNotification(
            id = "notif_1",
            title = "🍕 Free Delivery All Day!",
            message = "Enjoy 100% Free Home Delivery on all orders from Cheese Bites Restaurant.",
            timeAgo = "10m ago"
        ),
        AppNotification(
            id = "notif_2",
            title = "🔥 Mega Pizza Deals Available",
            message = "Check out our Pizza Deal 02 (XL Pizza, Wings, Nuggets, Fries & 1.5L drink) for just Rs. 2800!",
            timeAgo = "2h ago"
        ),
        AppNotification(
            id = "notif_3",
            title = "Welcome to Cheese Bite",
            message = "We are open from 12:00 PM till 01:00 AM daily. Fresh food baked just for you.",
            timeAgo = "1d ago"
        )
    )

    val defaultProfile = UserProfile(
        name = "Hamza Ali",
        phone = "0320-9163877",
        email = "hamza.ali@cheesebite.pk"
    )
}
