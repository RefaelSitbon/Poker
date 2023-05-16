package Poker.src;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class PokerHandler {
    public static ArrayList<CardNumEnum> sort(ArrayList<CardNumEnum> arr){
        arr.sort(new Comparator<CardNumEnum>() {
            @Override
            public int compare(CardNumEnum cardNumEnum1, CardNumEnum cardNumEnum2) {
                return cardNumEnum1.ordinal() - cardNumEnum2.ordinal();
            }

//            @Override
//            public int compare(Integer i1, Integer i2) {
//                return i1 - i2;
//            }
        });
        return arr;
    }

    public static void sortCard(ArrayList<Card> cards){
        cards.sort(Comparator.comparingInt(card -> card.num.ordinal()));
    }

    public static boolean checkStraightFlash(ArrayList<Card> cards){
        return checkFlush(cards, true);
    }

    public static boolean checkStraight(ArrayList<CardNumEnum> cards){
        int i = 1;
        int count = 0;
        if (cards.contains(CardNumEnum.AS)){
            cards.add(CardNumEnum.ONE);
        }
        cards = sort(cards);

        for(; i < cards.size(); ++i){
            if (cards.get(i).ordinal() == cards.get(i - 1).ordinal() + 1) {
                count += 1;
            } else if (cards.get(i).ordinal() == cards.get(i-1).ordinal()){

            }else if(count < 4){
                count = 0;
            }
        }

        return count >= 4;
    }

    public static boolean checkFourOfAKind(ArrayList<Card> cards){
        return checkIdenticalAmount(cards, 4);
    }

    public static boolean checkPair(ArrayList<Card> cards){
        return checkIdenticalAmount(cards, 2);
    }

    public static boolean checkTriple(ArrayList<Card> cards){
        return checkIdenticalAmount(cards, 3);
    }

    public static boolean checkFullHouse(ArrayList<Card> cards){
        return checkPair(cards) && checkTriple(cards);
    }

    public static boolean checkIdenticalAmount(ArrayList<Card> cards, int amount){
        boolean forReturn = false;
        HashMap<CardNumEnum, Integer> pairMap  = new HashMap<>();

        for (Card card: cards) {
            if(pairMap.get(card.num) == null){
                pairMap.put(card.num, 1);
            }else {
                pairMap.put(card.num, pairMap.get(card.num) + 1);
            }
        }

        for (CardNumEnum key: pairMap.keySet()) {
            if(pairMap.get(key) == amount){
                forReturn = true;
            }
        }

        return forReturn;
    }

    public static boolean checkFlush(ArrayList<Card> cards, boolean isStraightFlush){
        HashMap<CardTypeEnum, Integer> flashMap = new HashMap<>();
        HashMap<CardTypeEnum, ArrayList<CardNumEnum>> straightFlushMap = new HashMap<>();

        for (Card card: cards) {
            if(flashMap.get(card.type) == null){
                flashMap.put(card.type, 1);
                straightFlushMap.put(card.type, new ArrayList<>());
                straightFlushMap.get(card.type).add(card.num);
            }else {
                flashMap.put(card.type, flashMap.get(card.type) + 1);
                straightFlushMap.get(card.type).add(card.num);
            }
        }

        for (CardTypeEnum type: flashMap.keySet()) {
            if(flashMap.get(type) >= 5){
                if(isStraightFlush){
                    straightFlushMap.get(type).sort(new Comparator<CardNumEnum>() {
                        @Override
                        public int compare(CardNumEnum cardNumEnum1, CardNumEnum cardNumEnum2) {
                            return cardNumEnum1.ordinal() - cardNumEnum2.ordinal();
                        }

//                        @Override
//                        public int compare(Integer i1, Integer i2) {
//                            return i1 - i2;
//                        }
                    });
                    return checkStraight(straightFlushMap.get(type));
                }else {
                    return true;
                }
            }
        }
        return false;
    }
}
