package Poker.src;

import java.util.*;
import static Poker.src.Card.castStrToNum;
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
        initCardMap();
    }

    private  void initTypeArr(){
        typeArr.add(CardTypeEnum.Heart);typeArr.add(CardTypeEnum.Diamond);typeArr.add(CardTypeEnum.Club);
        typeArr.add(CardTypeEnum.Spades);
    }

    private  void initNumArr(){
        numArr.add(CardNumEnum.TWO);numArr.add(CardNumEnum.THREE);numArr.add(CardNumEnum.FOUR);numArr.add(CardNumEnum.FIVE);
        numArr.add(CardNumEnum.SIX);numArr.add(CardNumEnum.SEVEN);numArr.add(CardNumEnum.EIGHT);numArr.add(CardNumEnum.NINE);
        numArr.add(CardNumEnum.TEN);numArr.add(CardNumEnum.JACK);numArr.add(CardNumEnum.Queen);numArr.add(CardNumEnum.KING);
        numArr.add(CardNumEnum.AS);
    }

    private void initCardMap(){
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
                                                                        ArrayList<Card> cards){
        if(checkStraightFlash(cards)){ratingMap.get(HandRating.StraightFlash).add((ArrayList<Card>) cards.clone());}
        if(checkFourOfAKind(cards)){ratingMap.get(HandRating.FourOfAKind).add((ArrayList<Card>) cards.clone());}
        if(checkFullHouse(cards)){ratingMap.get(HandRating.FullHouse).add((ArrayList<Card>) cards.clone());}
        if(checkFlush(cards, false)){ratingMap.get(HandRating.Flush).add((ArrayList<Card>) cards.clone());}
        if(checkStraight(createArrOfInt(cards))){ratingMap.get(HandRating.Straight).add((ArrayList<Card>) cards.clone());}
        if(checkTriple(cards)){ratingMap.get(HandRating.Triple).add((ArrayList<Card>) cards.clone());}
        if(checkPair(cards)){ratingMap.get(HandRating.Pair).add((ArrayList<Card>) cards.clone());}
    }

    private Map<HandRating, ArrayList<ArrayList<Card>>> calculateOption(){
        Map<HandRating, ArrayList<ArrayList<Card>>> ratingMap = new HashMap<>();
        initRateMap(ratingMap);
        ArrayList<Card> cards = (ArrayList<Card>)this.cardsAndDeck.clone();
        Map<CardTypeEnum, ArrayList<CardNumEnum>> newDeckMap = new HashMap<>(cardMap);
        Card card = null;
        for (CardTypeEnum type: typeArr) {
            for (CardNumEnum num: numArr) {
                if(newDeckMap.get(type) != null && newDeckMap.get(type).contains(num)) {
                }else {
                    card = new Card(type, num);
                    cards.add(card);
                    PokerHandler.sortCard(cards);
                    newDeckMap.get(type).add(num);
                    for (CardTypeEnum type1: typeArr) {
                        for (CardNumEnum num1: numArr) {
                            if(newDeckMap.get(type1) != null && newDeckMap.get(type1).contains(num1)) {
                            }else {
                                checkOptionCard(ratingMap, (ArrayList<Card>) cards.clone());
                            }
                        }
                    }
                    newDeckMap.get(type).remove((CardNumEnum) num);

//                    cards.remove(card);
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
//                i -= 1;
            }else {
                return card;
//                addCard(card);
            }
        }
    }

    public void printRateDeck(HandRating handRating){
        Map<HandRating, ArrayList<ArrayList<Card>>> map = this.calculateOption();
        System.out.println("$$$$$$$$$ " + handRating.toString() + " $$$$$$$$$");
        for (ArrayList<Card> arr: map.get(handRating)) {
            for (Card c: arr) {
                System.out.print(c.num + " " + c.type + ",");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {

        Poker newPpker = new Poker(new ArrayList<>());

        for(int i = 0; i < 5; ++i) {
            newPpker.addCard(newPpker.createRandomCard());
        }
//        newPpker.print(card.type, newPpker.numArr.get(card.num));
        for (Card c: newPpker.cardsAndDeck) {
            System.out.print(c.num + " " + c.type + ",");
        }
        System.out.println();
//        System.out.println("StraightFlash: " + poker.checkStraightFlash(cardList));
//        System.out.println("checkStraight: " + poker.checkStraight(poker.createArrOfInt(cardList)));
//        System.out.println("FourOfACount: " + poker.checkFourOfAKind(cardList));
//        System.out.println("{checkPair: " + poker.checkPair(cardList));
//        System.out.println("checkTriple: " + poker.checkTriple(cardList) + " }");
//        System.out.println("checkFullHouse: " + poker.checkFullHouse(cardList));
//        System.out.println("checkFlush: " + poker.checkFlush(cardList, false));
//        newPpker.print();
//        newPpker.checkOptions();

        newPpker.printRateDeck(HandRating.StraightFlash);
        newPpker.printRateDeck(HandRating.FourOfAKind);
        newPpker.printRateDeck(HandRating.FullHouse);
        newPpker.printRateDeck(HandRating.Flush);
        newPpker.printRateDeck(HandRating.Straight);
        newPpker.printRateDeck(HandRating.Triple);
//        newPpker.printRateDeck(HandRating.Pair);
    }
}