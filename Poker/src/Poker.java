package Poker.src;

import java.util.*;
import static Poker.src.PokerHandler.*;

class Poker{
    private ArrayList<CardNumEnum> numArr = new ArrayList<>();
    private ArrayList<CardTypeEnum> typeArr = new ArrayList<>();
    private ArrayList<Card> cardsAndDeck;
    private Map<CardTypeEnum, ArrayList<CardNumEnum>> cardMap = new HashMap<>();

    Poker(ArrayList<Card> cards){
        cardsAndDeck = cards;
        initNumArr();
        initTypeArr();
        initCardMap(this.cardMap);
    }

    private  void initTypeArr(){
        typeArr.add(CardTypeEnum.Heart);typeArr.add(CardTypeEnum.Diamond);typeArr.add(CardTypeEnum.Club);
        typeArr.add(CardTypeEnum.Spades);
    }

    private  void initNumArr(){
        numArr.add(CardNumEnum.TWO);numArr.add(CardNumEnum.THREE);numArr.add(CardNumEnum.FOUR);numArr.add(CardNumEnum.FIVE);
        numArr.add(CardNumEnum.SIX);numArr.add(CardNumEnum.SEVEN);numArr.add(CardNumEnum.EIGHT);numArr.add(CardNumEnum.NINE);
        numArr.add(CardNumEnum.TEN);numArr.add(CardNumEnum.JACK);numArr.add(CardNumEnum.QUEEN);numArr.add(CardNumEnum.KING);
        numArr.add(CardNumEnum.AS);
    }

    private void initCardMap(Map<CardTypeEnum, ArrayList<CardNumEnum>> cardMap){
        cardMap.put(CardTypeEnum.Heart, new ArrayList<>());
        cardMap.put(CardTypeEnum.Diamond, new ArrayList<>());
        cardMap.put(CardTypeEnum.Club, new ArrayList<>());
        cardMap.put(CardTypeEnum.Spades, new ArrayList<>());
    }

    private void addCard(Card card){
        if(cardMap.get(card.type) == null){
            cardMap.put(card.type, new ArrayList<>());
        }
        cardMap.get(card.type).add(card.num);
        cardsAndDeck.add(card);

        sortCard();
    }

    private void sortCard (){
        PokerHandler.sortCard(this.cardsAndDeck);
    }

    private ArrayList<CardNumEnum> createArrOfInt(ArrayList<Card> cards){
        ArrayList<CardNumEnum> arr = new ArrayList<>();
        for (Card card: cards) {
            arr.add(card.num);
        }
        return arr;
    }

    private void checkOptions(){
        for (CardTypeEnum type: typeArr) {
            for (CardNumEnum num: numArr) {
                if(cardMap.get(type) != null && cardMap.get(type).contains(num)){

                }else {
                    print(type, num);
                }
            }
        }
    }

    private void print(CardTypeEnum type, CardNumEnum num){
        ArrayList<Card> cards = (ArrayList<Card>) this.cardsAndDeck.clone();
        Card card = new Card(type, num);

        cards.add(card);

        for (Card c: cards) {
            System.out.print(c.num + " " + c.type + ",");
        }
        System.out.println();
        if(checkStraightFlash(cards)){System.out.println("StraightFlash: ");}
        if(checkFourOfAKind(cards)){System.out.println("FourOfAKind: " );}
        if(checkFullHouse(cards)){System.out.println("FullHouse: " );}
        if(checkFlush(cards, false)){System.out.println("Flush: ");}
        if(checkStraight(createArrOfInt(cards))){System.out.println("Straight: ");}
        if(checkTriple(cards)){System.out.println("Triple: " );}
        if(checkPair(cards)){System.out.println("Pair: " );}
    }

    private Card cardFactory(int cardNum, int cardType){
        return new Card(typeArr.get(cardType), numArr.get(cardNum));
    }

    private void initRateMap(Map<HandRating, ArrayList<ArrayList<Card>>> ratingMap){
        ratingMap.put(HandRating.StraightFlash, new ArrayList<>());
        ratingMap.put(HandRating.FullHouse, new ArrayList<>());
        ratingMap.put(HandRating.Flush, new ArrayList<>());
        ratingMap.put(HandRating.FourOfAKind, new ArrayList<>());
        ratingMap.put(HandRating.Straight, new ArrayList<>());
        ratingMap.put(HandRating.Triple, new ArrayList<>());
        ratingMap.put(HandRating.Pair, new ArrayList<>());
    }

    private void checkOptionCard(Map<HandRating, ArrayList<ArrayList<Card>>> ratingMap,
                                                                        ArrayList<Card> cards, Card card1, Card card2){
        ArrayList<Card> cards1 = new ArrayList<>();
        cards1.add(card1);cards1.add(card2);
        if(checkStraightFlash(cards)){ratingMap.get(HandRating.StraightFlash).add(cards1);}
        if(checkFourOfAKind(cards)){ratingMap.get(HandRating.FourOfAKind).add(cards1);}
        if(checkFullHouse(cards)){ratingMap.get(HandRating.FullHouse).add(cards1);}
        if(checkFlush(cards, false)){ratingMap.get(HandRating.Flush).add(cards1);}
        if(checkStraight(createArrOfInt(cards))){ratingMap.get(HandRating.Straight).add(cards1);}
        if(checkTriple(cards)){ratingMap.get(HandRating.Triple).add(cards1);}
        if(checkPair(cards)){ratingMap.get(HandRating.Pair).add(cards1);}
    }

    private Map<CardTypeEnum, ArrayList<CardNumEnum>> cloneCardMap(){
        Map<CardTypeEnum, ArrayList<CardNumEnum>> newCardMap = new HashMap<>();
        initCardMap(newCardMap);
        for (CardTypeEnum type: cardMap.keySet()) {
            newCardMap.put(type,(ArrayList<CardNumEnum>) cardMap.get(type).clone());
        }
        return newCardMap;
    }

    private Map<HandRating, ArrayList<ArrayList<Card>>> calculateOption(boolean firstHit){
        Map<HandRating, ArrayList<ArrayList<Card>>> ratingMap = new HashMap<>();
        initRateMap(ratingMap);
        ArrayList<Card> cards = (ArrayList<Card>)this.cardsAndDeck.clone();
        Map<CardTypeEnum, ArrayList<CardNumEnum>> newDeckMap = cloneCardMap();
        Card card1 = null;
        Card card2 = null;
        for (CardTypeEnum type1: typeArr) {
            for (CardNumEnum num1: numArr) {
                if(newDeckMap.get(type1).contains(num1)) {
                }else {
                    card1 = new Card(type1, num1);
                    cards.add(card1);
                    PokerHandler.sortCard(cards);
                    newDeckMap.get(type1).add(num1);
                    if(firstHit) {
                        for (CardTypeEnum type2 : typeArr) {
                            for (CardNumEnum num2 : numArr) {
                                if (newDeckMap.get(type2).contains(num2)) {
                                } else {
                                    card2 = new Card(type2, num2);
                                    cards.add(card2);
                                    PokerHandler.sortCard(cards);
                                    checkOptionCard(ratingMap, (ArrayList<Card>) cards.clone(), card1, card2);
                                    cards.remove(card2);
                                }
                            }
                        }
                    }else {
                        checkOptionCard(ratingMap, (ArrayList<Card>) cards.clone(), card1, null);
                    }
                    cards.remove(card1);
                    newDeckMap.get(type1).remove(num1);
                }
            }
        }
        return ratingMap;
    }

    public Card createRandomCard(){
        Random random = new Random();
        int randomNum = 0;
        int randomType = 0;
        Card card = null;

        while (true){
            randomNum = random.nextInt(13);
            randomType = random.nextInt(4);
            card = cardFactory(randomNum, randomType);
            if(cardMap.get(card.type) != null && cardMap.get(card.type).contains(card.num)){
            }else {
//                addCard(card);
                return card;
            }
        }
    }

    public void printRateDeck(HandRating handRating, boolean firstHit){
        Map<HandRating, ArrayList<ArrayList<Card>>> map = calculateOption(firstHit);
        System.out.println("$$$$$$$$$ " + handRating.toString() + " $$$$$$$$$");
        for (ArrayList<Card> arr: map.get(handRating)) {
            for (Card c: arr) {
                System.out.print(c.num.ordinal() + 2 + " " + c.type + ",");
            }
            System.out.println();
        }
    }

    public void printAmountOfOption(HandRating handRating, boolean firstHit){
        Map<HandRating, ArrayList<ArrayList<Card>>> map = calculateOption(firstHit);
        System.out.println("######### " + handRating.toString() + " #########");

        if(firstHit && map.get(handRating).size() > 0) {
            System.out.println(map.get(handRating).size() + " from " + 2209 + " options, SUM: " + 100/(Float.intBitsToFloat(2209)/Float.intBitsToFloat(map.get(handRating).size())));
        } else if (map.get(handRating).size() > 0) {
            System.out.println(map.get(handRating).size() + " from " + 2116 + " options, SUM: " + 100/(Float.intBitsToFloat(2116)/Float.intBitsToFloat(map.get(handRating).size())));
        }
    }

    private void sendToPrintEveryCardOption(boolean firstHit){
        printRateDeck(HandRating.StraightFlash, firstHit);
        printRateDeck(HandRating.FourOfAKind, firstHit);
        printRateDeck(HandRating.FullHouse, firstHit);
        printRateDeck(HandRating.Flush, firstHit);
        printRateDeck(HandRating.Straight, firstHit);
        printRateDeck(HandRating.Triple, firstHit);
//        printRateDeck(HandRating.Pair);
    }

    private void sendToPrintEveryRate(boolean firstHit){
        printAmountOfOption(HandRating.StraightFlash, firstHit);
        printAmountOfOption(HandRating.FourOfAKind, firstHit);
        printAmountOfOption(HandRating.FullHouse, firstHit);
        printAmountOfOption(HandRating.Flush, firstHit);
        printAmountOfOption(HandRating.Straight, firstHit);
        printAmountOfOption(HandRating.Triple, firstHit);
//        printAmountOfOption(HandRating.Pair);
    }

    public static void runDemo(ArrayList<Card> cards){
        Poker poker = new Poker(cards);
        poker.sendToPrintEveryRate(true);
    }

    public static void runDemoAfterFirstHit(ArrayList<Card> cards){
        Poker poker = new Poker(cards);
        poker.sendToPrintEveryRate(false);
    }

    public static void main(String[] args) {
        ArrayList<Card> arrayList = new ArrayList<>();
        arrayList.add(new Card(CardTypeEnum.Spades, CardNumEnum.EIGHT));
        arrayList.add(new Card(CardTypeEnum.Spades, CardNumEnum.SIX));
        arrayList.add(new Card(CardTypeEnum.Club, CardNumEnum.EIGHT));
        arrayList.add(new Card(CardTypeEnum.Club, CardNumEnum.FOUR));
        arrayList.add(new Card(CardTypeEnum.Club, CardNumEnum.NINE));
        Poker.runDemo(arrayList);
        arrayList.add(new Card(CardTypeEnum.Club, CardNumEnum.AS));
//        Poker.runDemoAfterFirstHit(arrayList);



//        Poker newPpker = new Poker(new ArrayList<>());
//
//        for(int i = 0; i < 5; ++i) {
//            newPpker.addCard(newPpker.createRandomCard());
//        }
//        for (Card c: newPpker.cardsAndDeck) {
//            System.out.print((c.num.ordinal() + 2) + " " + c.type + ",");
//        }
//        System.out.println();
//
//        newPpker.sendToPrintEveryRate(true);
//        newPpker.addCard(newPpker.createRandomCard());
//        System.out.println("ADD ANOTHER CARD!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        for (Card c: newPpker.cardsAndDeck) {
//            System.out.print((c.num.ordinal() + 2) + " " + c.type + ",");
//        }
////        newPpker.sendToPrintEveryCardOption(true);
//        System.out.println();
//        newPpker.sendToPrintEveryRate(false);
    }
}