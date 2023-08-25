package com.example.perfumestore

import com.example.perfumestore.data.model.ProductItem

class LocalPerfumes{

    fun createList(): List<ProductItem>{
        val list: MutableList<ProductItem> = mutableListOf()
        var id = 0
        list.add(
            ProductItem(
                id++,
                R.drawable.kirke,
                "Kirke",
                "Tiziana Terenzi",
                300f,
                650f,
                1,
                25,
                "Мы создали чувственный и теплый аромат, который покорил нас, раскрывая наши души красоте чувств. Это волшебный и чувственный парфюм посвященный богине Цирцее. Духи Kirke' открываются фруктовыми сладостными и сочными нотами маракуйи, персиков, малины, черной смородины и груши, настоящий золотой алхимический эликсир. В сердце аромат поддерживает фруктовую феерию нотами очаровательного ландыша, а шлейф похож на прогулку по песку босыми ногами: нежный гелиотропин, сандал, ваниль, мускус и пачули. Аромат Kirke восхищает своего владельца как знаменитый очаровывающий эликсир богини Цирцеи, направляя по таинственным путям в поисках удивительных чудес. Он вдохновляет, создавая изысканную ауру утонченной чувственности."
            )
        )
        list.add(
            ProductItem(
                id++,
                R.drawable.tom_ford_lost_cherry,
                "Lost Cherry",
                "Tom Ford",
                300f,
                650f,
                2,
                25,
                "Сладость. Соблазн. Неутолимое желание. Lost Cherry — насыщенный аромат, открывающий двери в некогда запретный мир. Его соблазнительная двойственность основана на контрасте игривой, блестящей и гладкой изнанки с сочной мякотью внутри», — ТОМ ФОРД. Черешня, масло горького миндаля, сироп из вишни «гриот», турецкая роза, перуанский бальзам и обжаренные бобы тонка.\n" +
                        "\n" +
                        "Насыщенный и изысканный восточный аромат Lost Cherry пронизан контрастами. Он переносит нас в место, где невинность пересекается с наслаждением, а сладость встречается с соблазном.\n" +
                        "\n" +
                        "Начальные ноты Lost Cherry воссоздают притягательность и совершенство экзотических плодов черешни и ее спелой сияющей мякоти, источающей вишневый ликер с дразнящим привкусом горького миндаля. Затем раскрываются двойственные аккорды, в которых контрастируют сладкие и терпкие, яркие и таинственные ноты. Ноты сиропа из вишни «гриот» напоминают об аппетитных вымоченных ягодах, а звучание турецкой розы и арабского жасмина придает аромату глубокий землистый оттенок. Lost Cherry пробуждает неутолимое желание и будоражит фантазию, завершаясь величественным насыщенным шлейфом из чувственных нот перуанского бальзама, обжаренных бобов тонка, сандалового дерева, ветивера и кедра."
            )
        )
        list.add(
            ProductItem(
                id++,
                R.drawable.tom_ford_bitter_peach,
                "Bitter Peach",
                "Tom Ford",
                300f,
                650f,
                3,
                25,
                "Откровенно сладкий, темный и роскошный аромат Bitter Peach накрывает волной таинственной, обволакивающей чувственности. Он напоминает спелый и сочный фрукт, перед соблазнительным ароматом которого невозможно устоять», — ТОМ ФОРД. Tom Ford представляет новый аромат в коллекции Private Blend. Bitter Peach — это роскошный соблазнительный нектар из самых спелых, головокружительно сладких фруктов с манящим ароматом. Опьяняющая фруктово-цветочная композиция открывается аппетитными сладкими нотами персика, выросшего среди виноградников, и масла красного сицилийского апельсина. Острота пряного масла кардамона как будто вызывает ощущение сочной и спелой мякоти на языке. В чувственном сердце аромата горькие ноты гелиотропа и масла полыни переплетаются с головокружительными оттенками абсолюта рома и коньячного масла. Звучание абсолюта арабского жасмина подталкивает к беззастенчивому изучению своих желаний. Роскошное сочетание абсолюта сандалового дерева, бензоиновой смолы и кашмерана делают шлейф более насыщенным и выразительным. С нотами ванили, абсолюта бобов тонка и масла индонезийского пачули Bitter Peach не теряет своей соблазнительности. Изысканный дизайн флаконов Tom Ford Bitter Peach продуман до мельчайших деталей. Изнутри флакон объемом 50 мл покрыт перламутровым лаком, а снаружи выполнен из полупрозрачного стекла нежного оттенка. Флакон подчеркивает насыщенность и многогранность композиции и манит прикоснуться к миру сладких и соблазнительных ароматов. Оттенок флакона навеян разнообразными оттенками мякоти персика и винтажными цветными стеклами."
            )
        )
        list.add(
            ProductItem(
                id++,
                R.drawable.l1212,
                "L.12.12",
                "LACOSTE",
                300f,
                650f,
                4,
                25,
                "Парфюмерная вода Lacoste L.12.12 Blanc для него. Совершенно чуждый традиционным принципам, но при этом всегда актуальный аромат переливается контрастами, чтобы идеально подстроиться под своего владельца. Изящно искрящиеся оттенки цитрусовых сливаются с наполняющими энергией зелеными нотами. Тепло дерева вступает в противоборство со свежестью эвкалипта. Все это на фоне базовых успокаивающих нот кедра и кардамона."
            )
        )
        list.add(
            ProductItem(
                id++,
                R.drawable.tom_ford_tobacco_vanille,
                "Tobacco Vanille",
                "Tom Ford",
                300f,
                650f,
                5,
                25,
                "Аромат Tom Ford Tobacco Vanille вызывает ассоциации с английским клубом джентльменов и открывается с новой стороны благодаря неожиданным ингредиентам и ноткам специй. Tom Ford Tobacco Vanille — это богатый и теплый аромат с неожиданным и современным характером."
            )
        )
        return list
    }
}