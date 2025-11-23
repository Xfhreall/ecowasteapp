package com.example.ecowasteapp.data

import com.example.ecowasteapp.model.WasteItem

object DummyWasteData {

    val wasteItems = listOf(
        WasteItem(
            id = "plastic_bottle",
            name = "Botol Plastik",
            category = "Anorganik / Daur Ulang",
            description = "Botol plastik merupakan sampah anorganik yang dapat didaur ulang.",
            disposalInstruction = "Kosongkan isi, bilas, lalu masukkan ke tempat sampah khusus plastik atau daur ulang.",
            recycleInfo = "Botol plastik dapat didaur ulang menjadi bahan baku plastik baru, serat tekstil, atau produk lainnya.",
            binColorInfo = "Masukkan ke tempat sampah berwarna kuning atau biru (khusus plastik/daur ulang)."
        ),
        WasteItem(
            id = "food_waste",
            name = "Sisa Makanan",
            category = "Organik",
            description = "Sisa makanan termasuk sampah organik yang mudah terurai.",
            disposalInstruction = "Pisahkan dari plastik, masukkan ke tempat sampah organik.",
            recycleInfo = "Dapat diolah menjadi kompos bila diproses dengan benar.",
            binColorInfo = "Masukkan ke tempat sampah berwarna hijau (organik)."
        ),
        WasteItem(
            id = "paper",
            name = "Kertas",
            category = "Anorganik / Daur Ulang",
            description = "Kertas dapat didaur ulang menjadi kertas baru atau produk kertas lainnya.",
            disposalInstruction = "Pastikan kertas kering dan tidak terkontaminasi minyak atau makanan.",
            recycleInfo = "Kertas yang bersih dapat dikumpulkan dan dijual ke bank sampah atau didaur ulang.",
            binColorInfo = "Masukkan ke tempat sampah biru/kuning khusus kertas dan daur ulang."
        ),
        WasteItem(
            id = "glass_bottle",
            name = "Botol Kaca",
            category = "Anorganik / Daur Ulang",
            description = "Botol kaca adalah sampah anorganik yang dapat digunakan kembali atau didaur ulang.",
            disposalInstruction = "Bersihkan botol kaca dan pisahkan dari bahan lain.",
            recycleInfo = "Kaca dapat dilebur dan dibentuk kembali menjadi produk kaca baru.",
            binColorInfo = "Masukkan ke tempat sampah khusus kaca atau daur ulang (biasanya biru/kuning)."
        ),
        WasteItem(
            id = "battery",
            name = "Baterai Bekas",
            category = "B3 (Bahan Berbahaya dan Beracun)",
            description = "Baterai bekas mengandung bahan kimia yang dapat mencemari tanah dan air.",
            disposalInstruction = "Jangan dibuang ke tempat sampah biasa. Kumpulkan dan bawa ke titik pengumpulan B3.",
            recycleInfo = "Beberapa komponen dapat didaur ulang, namun harus melalui pengelolaan khusus.",
            binColorInfo = "Masukkan ke tempat khusus limbah B3, biasanya berwarna merah atau diberi simbol berbahaya."
        )
    )
}
