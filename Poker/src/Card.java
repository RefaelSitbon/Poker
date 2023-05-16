package Poker.src;

public class Card implements Cloneable{
    public CardTypeEnum type;
    public CardNumEnum num;
    public boolean isAs = false;

    public Card(CardTypeEnum type, CardNumEnum num){
        this.type = type;
        this.num = num;
//        if(num == "King" || num == "Queen" || num == "Prince"){
//            if (num == "King"){
//                this.num = 13;
//            } else if (num == "Queen") {
//                this.num = 12;
//            }else {
//                this.num = 11;
//            }
//        } else if (num == "As") {
//            isAs = true;
//            this.num = 1;
//        }else {
//            this.num = castStrToNum(num);
//        }
    }

    public static int castStrToNum(String num){
        switch (num.toLowerCase()){
            case "as":
                return 1;
            case "two":
                return 2;
            case "three":
                return 3;
            case "four":
                return 4;
            case "five":
                return 5;
            case "six":
                return 6;
            case "seven":
                return 7;
            case "eight":
                return 8;
            case "nine":
                return 9;
            case "ten":
                return 10;
            case "prince":
                return 11;
            case "queen":
                return 12;
            case "king":
                return 13;
            default:
                return 0;
        }
    }

    @Override
    public Card clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (Card) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}