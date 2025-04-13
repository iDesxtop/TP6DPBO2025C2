# Janji
Saya Muhammad Alvinza dengan NIM 2304879 mengerjakan Tugas Praktikum 6 dalam mata kuliah Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.

## Desain Program
Program ini dibagi menjadi 4 kelas, yaitu App.java, FlappyBird.java, Player.java, dan Pipe.java.  
App.java: Titik masuk game  
FlappyBird.java: Panel game utama yang mengurus logika permainan  
Player.java: Representasi burung  
Pipe.java: Representasi rintangan pipa  

## Alur Program
Alur dibagi menjadi 3, Pre-Game, Game, dan Game Over.

### Pre-Game
Pada pre-game, player perlu memencet "spasi" untuk memulai permainan. Kondisi permainan berada pada posisi freeze, yaitu burung diam di tempat dan pipa belum muncul sebelum game dimulai.  

### Game
Pada Game, player sudah memencet "spasi" dan game sudah dimulai. Player dapat memencet spasi untuk menerbangkan burung dan melewati rintangan pipa yang bermunculan. Jika player berhasil melewati pipa, maka skor di atas layar akan bertambah 1.

### Game Over
Game Over akan terjadi ketika burung mengenai pipa atau jatuh ke bawah layar. Pada state ini, player hanya dapat merestart game dengan memencet "R", dan game pun akan reset ke Pre-Game.



