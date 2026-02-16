package Game;

public class Main {

    // try-catch kullanmamak için buraya "throws Exception" ekledik
    public static void main(String[] args) throws Exception {
        
        // 1. Önce oyun tahtamızı (haritayı ve duvarları) hafızada oluşturuyoruz
        GameBoard board = new GameBoard(55,25);
        
        // 2. Sonra bu haritayı Oyun Kontrolcümüze gönderip motoru çalıştırıyoruz
        GameControls game = new GameControls(board);
        
    }
}