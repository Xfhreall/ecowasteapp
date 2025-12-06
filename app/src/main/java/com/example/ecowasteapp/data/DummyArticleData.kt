package com.example.ecowasteapp.data

import com.example.ecowasteapp.model.Article

object DummyArticleData {
    val articles = listOf(
        Article("1", "Mengapa Memilah Sampah?", "Memilah sampah membantu proses daur ulang menjadi lebih efektif dan mengurangi tumpukan sampah di TPA."),
        Article("2", "Bahaya Sampah Plastik", "Sampah plastik butuh ratusan tahun untuk terurai dan dapat menjadi mikroplastik yang mencemari lautan."),
        Article("3", "Apa itu 3R?", "3R adalah Reduce (Kurangi), Reuse (Gunakan Kembali), dan Recycle (Daur Ulang)."),
        Article("4", "Kompos dari Sisa Makanan", "Sisa makanan bisa diubah menjadi pupuk kompos yang menyuburkan tanaman.")
    )
}