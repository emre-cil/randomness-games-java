/*----------------------------------------------
 * @author Emre Çil
 * @since 08.05.2020
 * GitHub: https://github.com/Kheseyroon
 * MIT license: Copyright 2020 Emre Cil
 * to run -> javac CasinoGames.java
 *        -> java CasinoGames
 * ----------------------------------------------- */

import java.util.Scanner;
import java.util.UUID;

public class CasinoGames {

    public static void main(String[] args) {
        Player user1 = new Player("Emre", 18, 0);
        Scanner input = new Scanner(System.in);
        Roulette roulette = new Roulette();
        CoinFlip coinFlip = new CoinFlip();
        Dice dice = new Dice();
        App app = new App(user1, roulette, dice, coinFlip);

        System.out.println(user1.getName() + "-->" + user1.getId());

        System.out.println("-----Casino-----");
        while (true) {
            System.out.println(user1.getName() + " your current fund is " + user1.getFunds());
            System.out.println("please enter where do you want to go: ");
            System.out.println("1 - Deposit \n2 - Withdraw \n3 - Redeem code  \n4 - Games\n0 - Exit");
            byte inputCode = input.nextByte();
            if (inputCode < 1 || inputCode > 5) {
                break;
            } else if (inputCode == 1) {
                System.out.println("enter how much money do you want to deposit:");
                user1.deposit(input.nextDouble());
            } else if (inputCode == 2) {
                System.out.println("enter how much money do you want to withdraw:");
                user1.withdraw(input.nextDouble());
            } else if (inputCode == 3) {
                System.out.println("enter code(hint: you can use 'FREE'):");
                user1.giftCode(input.next());
            } else if (inputCode == 4)
                app.gamesMenu();
            else System.exit(0);

        }

    }
}


abstract class Games {
    private int luckyNumber;
    private double betAmount;
    private int betCode;
    private int winCounter;

    public int getWinCounter() {
        return winCounter;
    }

    public void setWinCounter(int winCounter) {
        this.winCounter = winCounter;
    }


    public int getLuckyNumber() {
        return luckyNumber;
    }

    public void setLuckyNumber(int luckyNumber) {
        this.luckyNumber = luckyNumber;
    }

    public double getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(double betAmount) {
        this.betAmount = betAmount;
    }

    public int getBetCode() {
        return betCode;
    }

    public void setBetCode(int betCode) {
        this.betCode = betCode;
    }

    public abstract void bet(double amount, int code, Player player);

    public abstract void play(Player player);
}

interface IBonusFund {
    void winTrain(Player player);
}

class Roulette extends Games implements IBonusFund {
    private String resultColor;
    private int resultCode;

    //    black = {1,2,3,4,5,6,7,8,9,10,11,12,1,14};
    //    red = {15,16,17,18,19,20,21,22,23,24,25,26,27,28};
    //    green = {29,30};
    //    //black code : 1
    //    //red code : 2
    //    //green code : 3

    public void bet(double amount, int color, Player player) {
        setLuckyNumber(((int) (Math.random() * 30) + 1));
        if (getLuckyNumber() > 0 && getLuckyNumber() < 15) {
            resultColor = "black";
            resultCode = 1;
        } else if (getLuckyNumber() > 14 && getLuckyNumber() < 29) {
            resultColor = "red";
            resultCode = 2;
        } else if (getLuckyNumber() > 28 && getLuckyNumber() < 31) {
            resultColor = "green";
            resultCode = 3;
        }
        setBetAmount(amount);
        setBetCode(color);
        player.setFunds(player.getFunds() - amount);
    }

    @Override
    public void play(Player player) {
        System.out.println("Color is " + resultColor);
        if (getBetCode() == resultCode) {
            setWinCounter(getWinCounter() + 1);
            winTrain(player);
            System.out.println(" ***********\n*You Win !!!*\n ***********\n");
            if (getBetCode() == 3)
                player.setFunds(player.getFunds() + (getBetAmount() * 10));
            else
                player.setFunds(player.getFunds() + (getBetAmount() * 2));
        } else {
            System.out.println(" ************\n*You Lost !!!*\n ************\n");
            setWinCounter(0);
        }
    }

    @Override
    public void winTrain(Player player) {
        if (getWinCounter() == 3) {
            player.setFunds(player.getFunds() + (getBetAmount() / 2));
            System.out.println("Congratulations, You have won 3 games in a row. " + getBetAmount() / 2 + " fund added.");
        }
    }
}

class Dice extends Games implements IBonusFund {
    //  1 2 3 4 5 6    classic numbers gives 7x
    //  code 7 = between 1-3 -------  code 8 = between 4-6   2x
    public void bet(double amount, int code, Player player) {
        setLuckyNumber(((int) (Math.random() * 6) + 1));
        setBetAmount(amount);
        setBetCode(code);
        player.setFunds(player.getFunds() - amount);
    }

    @Override
    public void play(Player player) {
        System.out.println("Lucky Number is " + getLuckyNumber());
        if (getBetCode() == getLuckyNumber() && getBetCode() < 7) {
            System.out.println(" ***********\n*You Win !!!*\n ***********\n");
            setWinCounter(getWinCounter() + 1);
            player.setFunds(player.getFunds() + (getBetAmount() * 7));
        } else if (getBetCode() == 7 && getLuckyNumber() < 4 && getLuckyNumber() > 0) {
            System.out.println(" ***********\n*You Win !!!*\n ***********\n");
            setWinCounter(getWinCounter() + 1);
            winTrain(player);
            player.setFunds(player.getFunds() + (getBetAmount() * 2));
        } else if (getBetCode() == 8 && getLuckyNumber() < 7 && getLuckyNumber() > 3) {
            System.out.println(" ***********\n*You Win !!!*\n ***********\n");
            setWinCounter(getWinCounter() + 1);
            winTrain(player);
            player.setFunds(player.getFunds() + (getBetAmount() * 2));
        } else {
            System.out.println(" ************\n*You Lost !!!*\n ************\n");
            setWinCounter(0);
        }
    }

    @Override
    public void winTrain(Player player) {
        if (getWinCounter() == 3) {
            player.setFunds(player.getFunds() + (getBetAmount() / 2));
            System.out.println("Congratulations, You have won 3 games in a row. " + getBetAmount() / 2 + " fund added.");
        }
    }
}

class CoinFlip extends Games implements IBonusFund {

    @Override
    public void bet(double amount, int code, Player player) {
        setLuckyNumber(((int) (Math.random() * 2) + 1));
        setBetAmount(amount);
        setBetCode(code);
        player.setFunds(player.getFunds() - amount);
    }

    @Override
    public void play(Player player) {
        System.out.println("It's " + ((getLuckyNumber() == 1) ? "Heads" : "Tails"));
        if (getBetCode() == getLuckyNumber()) {
            System.out.println(" ***********\n*You Win !!!*\n ***********\n");
            setWinCounter(getWinCounter() + 1);
            player.setFunds(player.getFunds() + (getBetAmount() * 2));
        } else {
            System.out.println(" ************\n*You Lost !!!*\n ************\n");
            setWinCounter(0);
        }
    }

    @Override
    public void winTrain(Player player) {
        if (getWinCounter() == 5) {
            player.setFunds(player.getFunds() + (getBetAmount() * 50));
            System.out.println("Congratulations, You have won 3 games in a row. " + getBetAmount() / 2 + " fund added.");
        }
    }
}

class App {
    private Player player;
    private Roulette roulette;
    private Dice dice;
    private CoinFlip coinFlip;
    private byte gameCode;
    private double betAmount = 0;
    Scanner input = new Scanner(System.in);

    public App(Player player, Roulette roulette, Dice dice, CoinFlip coinFlip) {
        this.player = player;
        this.roulette = roulette;
        this.dice = dice;
        this.coinFlip = coinFlip;
    }

    public void gamesMenu() {
        System.out.println("-----Games-----");
        while (true) {
            System.out.println("please enter which game you want to play: ");
            System.out.println("1 - Roulette \n2 - Dice \n3 - Coin Flip\n0 - Back to main menu");
            byte gameCode = input.nextByte();
            if (gameCode < 1 || gameCode > 3)
                break;
            else if (gameCode == 1)
                rouletteMenu();
            else if (gameCode == 2)
                diceMenu();
            else coinFlipMenu();
        }
    }

    public void rouletteMenu() {
        System.out.println("\nWelcome to roulette");

        while (true) {
            System.out.println(player.getName() + " your current fund is " + player.getFunds());
            System.out.println("please enter which color you want to bet");
            System.out.println("1 - black (2x)\n2 - red (2x)\n3 - green (10x)\n0 - exit");
            gameCode = input.nextByte();
            if (gameCode < 1 || gameCode > 3)
                break;

            System.out.println("please enter how much you want to bet");
            betAmount = input.nextDouble();
            betTester(roulette, player);
        }
    }

    public void diceMenu() {
        System.out.println("Welcome to dice");
        while (true) {
            System.out.println(player.getName() + " your current fund is " + player.getFunds());
            System.out.println("please enter which number you want to bet");
            System.out.println("1 - number 1 (7x)\n2 - number 2 (7x)\n3 - number 3 (7x)" +
                    "\n4 - number 4 (7x)\n5 - number 5 (7x)\n6 - number 6 (7x)" +
                    "\n7 - number between 1-3 (2x)\n8 - number between 4-6 (2x)\n0 - back to main menu");
            gameCode = input.nextByte();
            if (gameCode < 1 || gameCode > 8)
                break;

            System.out.println("please enter how much you want to bet");
            betAmount = input.nextDouble();
            betTester(dice, player);
        }
    }

    public void coinFlipMenu() {
        System.out.println("Welcome to dice");
        while (true) {
            System.out.println(player.getName() + " your current fund is " + player.getFunds());
            System.out.println("please enter which number you want to bet");
            System.out.println("1 - Heads (2x)\n2 - Tails (2x)\n0 - back to main menu");
            gameCode = input.nextByte();
            if (gameCode < 1 || gameCode > 2)
                break;

            System.out.println("please enter how much you want to bet");
            betAmount = input.nextDouble();
            betTester(coinFlip, player);
        }
    }

    public void betTester(Games game, Player player) {
        if (betAmount <= player.getFunds() && betAmount > 0) {
            game.bet(betAmount, gameCode, player);
            game.play(player);
        } else
            throw new InvaildBetException(betAmount, player.getFunds());

    }
}


class Player {
    private String name;
    private String Id;
    private int age;
    private double funds;
    private boolean giftCodeBlocker = false;


    public Player(String name, int age, double funds) {
        this.name = name;
        Id = UUID.randomUUID().toString();
        this.age = age;
        this.funds = funds;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return Id;
    }

    public int getAge() {
        return age;
    }

    public double getFunds() {
        return funds;
    }

    public void setFunds(double funds) {
        this.funds = funds;
    }

    public void deposit(double money) {
        if (money > 0)
            this.funds += money;
        else
            throw new InvalidDepositException(money);
    }

    public void withdraw(double money) {
        if (money > 0 && money <= funds) {
            this.funds -= money;
        } else
            throw new InvalidWithdrawException(money);
    }

    public void giftCode(String code) {
        System.out.println("--" + code + "--");
        if (code.equalsIgnoreCase("free") && !giftCodeBlocker) {
            funds += 100;
            giftCodeBlocker = true;
        } else if (giftCodeBlocker)
            System.out.println("You have already used free code.");
        else
            System.out.println("Wrong free code.");
    }

}

class InvalidWithdrawException extends RuntimeException {
    double amount;

    /**
     * Creates a new NotEnoughPaymentException object with given parameters.
     *
     * @param amount ,is amount which customer gave.
     */
    public InvalidWithdrawException(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Error: Invalid Withdraw Exception{ amount must be positive. -->" + amount + " }";
    }
}

class InvalidDepositException extends RuntimeException {
    double amount;

    /**
     * Creates a new NotEnoughPaymentException object with given parameters.
     *
     * @param amount ,is amount which customer gave.
     */
    public InvalidDepositException(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Error: Invalid Deposit Exception{ amount must be positive. -->  " + amount + " }";
    }
}

class InvaildBetException extends RuntimeException {
    double amount;
    double fund;

    /**
     * Creates a new NotEnoughPaymentException object with given parameters.
     *
     * @param amount ,is amount which customer gave.
     */
    public InvaildBetException(double amount, double fund) {
        this.amount = amount;
        this.fund = fund;
    }

    @Override
    public String toString() {
        if (amount < 0)
            return "Error: Invalid Bet Exception{ amount must be positive. -->" + amount + " }";
        else
            return "Error: Invalid Bet Exception{ your fund --> " + fund + " Please deposit" +
                    " at least --> " + (amount - fund) + " coin }";
    }
}
