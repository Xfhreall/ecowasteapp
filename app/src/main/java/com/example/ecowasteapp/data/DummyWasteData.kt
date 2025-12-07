package com.example.ecowasteapp.data

import com.example.ecowasteapp.model.WasteItem

object DummyWasteData {

    val wasteItems = listOf(
        // ============ KATEGORI 1: ORGANIK (3 items) ============
        WasteItem(
            id = "food_waste",
            name = "Sisa Makanan",
            category = "Organik",
            description = "Sisa makanan termasuk sampah organik yang mudah terurai seperti nasi, sayuran, buah-buahan, dan lauk pauk.",
            disposalInstruction = "Pisahkan dari sampah plastik atau anorganik. Masukkan ke tempat sampah organik.",
            recycleInfo = "Dapat diolah menjadi kompos atau pupuk organik bila diproses dengan benar. Bisa juga dijadikan pakan ternak.",
            binColorInfo = "Masukkan ke tempat sampah berwarna HIJAU (organik)."
        ),
        WasteItem(
            id = "fruit_peel",
            name = "Kulit Buah",
            category = "Organik",
            description = "Kulit buah seperti pisang, jeruk, apel, dan buah-buahan lainnya yang dapat terurai secara alami.",
            disposalInstruction = "Buang ke tempat sampah organik. Jangan dicampur dengan plastik atau kaca.",
            recycleInfo = "Sangat baik untuk dijadikan kompos. Kulit pisang kaya kalium, baik untuk tanaman.",
            binColorInfo = "Masukkan ke tempat sampah berwarna HIJAU (organik)."
        ),
        WasteItem(
            id = "leaves",
            name = "Daun Kering",
            category = "Organik",
            description = "Daun-daun yang gugur dari pohon atau tanaman hias. Termasuk sampah organik yang mudah terurai.",
            disposalInstruction = "Kumpulkan dan masukkan ke tempat sampah organik atau langsung ke komposter.",
            recycleInfo = "Sangat baik sebagai bahan kompos coklat (karbon). Dapat dicampur dengan sampah hijau untuk kompos seimbang.",
            binColorInfo = "Masukkan ke tempat sampah berwarna HIJAU (organik)."
        ),

        // ============ KATEGORI 2: PLASTIK (3 items) ============
        WasteItem(
            id = "plastic_bottle",
            name = "Botol Plastik",
            category = "Plastik",
            description = "Botol plastik air mineral, minuman ringan, atau kemasan plastik lainnya dengan kode daur ulang #1 (PET/PETE).",
            disposalInstruction = "Kosongkan isi, bilas dengan air, keringkan, lalu masukkan ke tempat sampah daur ulang.",
            recycleInfo = "Dapat didaur ulang menjadi bahan baku plastik baru, serat tekstil untuk pakaian, tas, atau produk lainnya. Nilai jual: Rp 3.000-5.000/kg.",
            binColorInfo = "Masukkan ke tempat sampah berwarna KUNING (plastik/daur ulang)."
        ),
        WasteItem(
            id = "plastic_bag",
            name = "Kantong Plastik",
            category = "Plastik",
            description = "Kantong plastik belanja atau kresek yang sering digunakan sehari-hari. Termasuk plastik #2 atau #4.",
            disposalInstruction = "Kumpulkan dalam kondisi bersih dan kering. Hindari kantong plastik yang kotor atau berminyak.",
            recycleInfo = "Bisa didaur ulang tapi nilainya rendah. Lebih baik dikurangi penggunaannya. Gunakan tas belanja reusable. Nilai jual: Rp 500-1.000/kg.",
            binColorInfo = "Masukkan ke tempat sampah berwarna KUNING (plastik/daur ulang)."
        ),
        WasteItem(
            id = "styrofoam",
            name = "Styrofoam",
            category = "Plastik",
            description = "Wadah makanan atau kemasan dari styrofoam/gabus. Sulit terurai dan berbahaya bagi lingkungan.",
            disposalInstruction = "Bersihkan dari sisa makanan. Kumpulkan secara terpisah karena sangat ringan dan mudah terbang.",
            recycleInfo = "Sangat sulit didaur ulang di Indonesia. Hindari penggunaan styrofoam sebisa mungkin. Gunakan alternatif ramah lingkungan.",
            binColorInfo = "Masukkan ke tempat sampah berwarna KUNING (plastik) atau ABU-ABU (residu)."
        ),

        // ============ KATEGORI 3: KERTAS (3 items) ============
        WasteItem(
            id = "cardboard",
            name = "Kardus",
            category = "Kertas",
            description = "Kardus bekas kemasan produk seperti kotak elektronik, paket belanja online, atau kemasan makanan.",
            disposalInstruction = "Ratakan kardus agar tidak memakan banyak tempat. Pastikan kering dan bersih dari lakban atau stiker.",
            recycleInfo = "Nilai daur ulang tinggi! Bisa dijual ke bank sampah atau pemulung. Akan didaur ulang menjadi kardus baru. Nilai jual: Rp 1.500-2.500/kg.",
            binColorInfo = "Masukkan ke tempat sampah berwarna BIRU (kertas/kardus)."
        ),
        WasteItem(
            id = "newspaper",
            name = "Koran Bekas",
            category = "Kertas",
            description = "Koran, majalah, atau kertas berita yang sudah tidak terpakai.",
            disposalInstruction = "Kumpulkan dan ikat dengan rapi. Pastikan kering dan tidak terkena air atau minyak.",
            recycleInfo = "Bisa dijual ke bank sampah. Akan didaur ulang menjadi kertas koran baru atau kertas tissue. Nilai jual: Rp 1.000-1.500/kg.",
            binColorInfo = "Masukkan ke tempat sampah berwarna BIRU (kertas/kardus)."
        ),
        WasteItem(
            id = "office_paper",
            name = "Kertas HVS",
            category = "Kertas",
            description = "Kertas putih HVS, kertas printer, fotokopi, atau kertas kantor lainnya.",
            disposalInstruction = "Kumpulkan kertas yang sudah tidak terpakai. Pisahkan dari plastik atau klip kertas.",
            recycleInfo = "Nilai daur ulang paling tinggi di antara jenis kertas lainnya. Bisa menjadi kertas HVS baru. Nilai jual: Rp 2.000-3.000/kg.",
            binColorInfo = "Masukkan ke tempat sampah berwarna BIRU (kertas/kardus)."
        ),

        // ============ KATEGORI 4: KACA & LOGAM (3 items) ============
        WasteItem(
            id = "glass_bottle",
            name = "Botol Kaca",
            category = "Kaca & Logam",
            description = "Botol kaca bekas minuman, parfum, atau kemasan makanan. Bisa digunakan kembali atau didaur ulang.",
            disposalInstruction = "Bersihkan botol kaca dari label dan sisa isi. Hati-hati saat menangani untuk menghindari pecah.",
            recycleInfo = "Kaca dapat dilebur dan dibentuk kembali menjadi produk kaca baru tanpa menurunkan kualitas. Nilai jual: Rp 500-1.000/kg.",
            binColorInfo = "Masukkan ke tempat sampah berwarna BIRU atau KUNING (daur ulang)."
        ),
        WasteItem(
            id = "aluminum_can",
            name = "Kaleng Aluminium",
            category = "Kaca & Logam",
            description = "Kaleng minuman ringan, bir, atau kemasan aluminium lainnya.",
            disposalInstruction = "Bilas kaleng dari sisa minuman. Bisa dipipihkan agar hemat tempat.",
            recycleInfo = "Aluminium sangat bernilai tinggi! 100% dapat didaur ulang berkali-kali tanpa menurun kualitas. Nilai jual: Rp 15.000-20.000/kg.",
            binColorInfo = "Masukkan ke tempat sampah berwarna KUNING (logam/daur ulang)."
        ),
        WasteItem(
            id = "tin_can",
            name = "Kaleng Besi",
            category = "Kaca & Logam",
            description = "Kaleng makanan seperti sarden, susu, atau kornet. Terbuat dari besi/baja yang dilapisi timah.",
            disposalInstruction = "Kosongkan isi, bilas, dan keringkan. Lepaskan label kertas jika memungkinkan.",
            recycleInfo = "Dapat didaur ulang menjadi baja baru untuk konstruksi atau produk logam lainnya. Nilai jual: Rp 2.000-3.000/kg.",
            binColorInfo = "Masukkan ke tempat sampah berwarna KUNING (logam/daur ulang)."
        ),

        // ============ KATEGORI 5: B3 - BAHAN BERBAHAYA (3 items) ============
        WasteItem(
            id = "battery",
            name = "Baterai Bekas",
            category = "B3 (Berbahaya)",
            description = "Baterai bekas (AA, AAA, atau baterai kancing) mengandung bahan kimia berbahaya seperti merkuri, kadmium, dan timbal.",
            disposalInstruction = "JANGAN dibuang ke tempat sampah biasa! Kumpulkan secara terpisah dan bawa ke drop point B3 terdekat.",
            recycleInfo = "Beberapa komponen dapat didaur ulang oleh pengelola limbah B3 bersertifikat. Cari drop point di mall atau kantor pemerintah.",
            binColorInfo = "Masukkan ke tempat khusus limbah B3 berwarna MERAH atau diberi simbol tengkorak."
        ),
        WasteItem(
            id = "fluorescent_lamp",
            name = "Lampu Neon",
            category = "B3 (Berbahaya)",
            description = "Lampu neon atau lampu hemat energi mengandung merkuri yang berbahaya jika pecah dan terhirup.",
            disposalInstruction = "Bungkus dengan koran atau kardus agar tidak pecah. Bawa ke drop point limbah B3, jangan dibuang sembarangan.",
            recycleInfo = "Harus dikelola oleh perusahaan pengelola limbah B3 bersertifikat. Merkuri dapat didaur ulang.",
            binColorInfo = "Masukkan ke tempat khusus limbah B3 berwarna MERAH."
        ),
        WasteItem(
            id = "electronic_waste",
            name = "Elektronik Rusak",
            category = "B3 (Berbahaya)",
            description = "Barang elektronik rusak seperti HP lama, charger rusak, kabel, atau komponen elektronik lainnya.",
            disposalInstruction = "Jangan dibuang ke tempat sampah biasa. Bawa ke drop point e-waste atau tukang reparasi elektronik.",
            recycleInfo = "Mengandung logam berharga (emas, tembaga, perak) dan bahan berbahaya. Harus dikelola oleh pengelola e-waste bersertifikat.",
            binColorInfo = "Masukkan ke tempat khusus e-waste atau drop point B3."
        )
    )
    
    // Fungsi helper untuk filter by category
    fun getByCategory(category: String): List<WasteItem> {
        return wasteItems.filter { it.category.contains(category, ignoreCase = true) }
    }
    
    // Semua kategori yang tersedia
    val categories = listOf(
        "Organik",
        "Plastik", 
        "Kertas",
        "Kaca & Logam",
        "B3 (Berbahaya)"
    )
}
