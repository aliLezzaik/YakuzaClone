import java.io.*;
import java.util.LinkedList;

public class Player implements Serializable{
    private int yens;
    private Player() {
        yens = 100;
    }
    public String getMoney() {
        return "¥"+yens;
    }
    public int getMoneyAmount() {
        return yens;
    }
    private static void savePlayer(Player player)throws Exception {
        File file = new File("save/playerSave.bin");
        ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file));
        obj.flush();
        obj.writeObject(player);
        obj.close();
    }
    public static Player player(){
        try{
            ObjectInputStream obj = new ObjectInputStream(new FileInputStream(new File("save/playerSave.bin")));
            return (Player) obj.readObject();
        }catch (Exception err) {
            Player player = new Player();
            try {
                savePlayer(player);
            }catch (Exception err2) {
                System.out.println("Error 404");
            }
            System.out.println("New");
            return player;
        }
    }
    public void modifyMoney(int newAmount) {
        yens = newAmount;
        try{
            savePlayer(this);
        }catch (Exception error) {
            System.out.println("Error 404");
        }

    }
}
