package com.example.ecowasteapp.data

import com.example.ecowasteapp.model.Article

object DummyArticleData {
    val articles = listOf(
        Article(
            id = "1",
            title = "Mengapa Memilah Sampah?",
            content = "Memilah sampah membantu proses daur ulang menjadi lebih efektif dan mengurangi tumpukan sampah di TPA.",
            imageUrl = "https://images.unsplash.com/photo-1532996122724-e3c354a0b15b?w=800&q=80",
            articleUrl = "https://www.kompas.com/homey/read/2022/02/18/070000776/pentingnya-memilah-sampah-organik-dan-anorganik-apa-saja-manfaatnya-"
        ),
        Article(
            id = "2",
            title = "Bahaya Sampah Plastik",
            content = "Sampah plastik butuh ratusan tahun untuk terurai dan dapat menjadi mikroplastik yang mencemari lautan.",
            imageUrl = "https://images.unsplash.com/photo-1621451537084-482c73073a0f?w=800&q=80",
            articleUrl = "https://www.wwf.or.id/publikasi/plastik-ancaman-bagi-lingkungan-dan-kesehatan/"
        ),
        Article(
            id = "3",
            title = "Apa itu 3R?",
            content = "3R adalah Reduce (Kurangi), Reuse (Gunakan Kembali), dan Recycle (Daur Ulang).",
            imageUrl = "https://images.unsplash.com/photo-1611284446314-60a58ac0deb9?w=800&q=80",
            articleUrl = "https://dlh.semarangkota.go.id/reduce-reuse-recycle-3r-sampah-mandiri/"
        ),
        Article(
            id = "4",
            title = "Kompos dari Sisa Makanan",
            content = "Sisa makanan bisa diubah menjadi pupuk kompos yang menyuburkan tanaman.",
            imageUrl = "https://images.unsplash.com/photo-1625246333195-78d9c38ad449?w=800&q=80",
            articleUrl = "https://www.kompas.com/homey/read/2021/09/14/170000776/cara-membuat-pupuk-kompos-dari-sampah-dapur"
        )
    )
}