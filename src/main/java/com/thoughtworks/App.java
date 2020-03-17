package com.thoughtworks;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {

  public static void main(String[] args) {
    // 以下是执行交易的交易员和发生的一系列交易,请为以下八个查询方法提供实现。
    Trader raoul = new Trader("Raoul", "Cambridge");
    Trader mario = new Trader("Mario", "Milan");
    Trader alan = new Trader("Alan", "Cambridge");
    Trader brian = new Trader("Brian", "Cambridge");
    List<Transaction> transactions = Arrays.asList(new Transaction(brian, 2011, 300),
        new Transaction(raoul, 2012, 1000),
        new Transaction(raoul, 2011, 400),
        new Transaction(mario, 2012, 710), new Transaction(mario, 2012, 700),
        new Transaction(alan, 2012, 950)
    );

    // 1.找出2011年的所有交易并按交易额排序(从低到高)
    System.out.println(get2011Transactions(transactions));

    // 2.交易员都在哪些不同的􏱜城市工作过
    System.out.println(getTradersCity(transactions));

    // 3.查找所有来自于剑桥的交易员，并按姓名排序
    System.out.println(getCambridgeTraders(transactions));

    // 4.返回所有交易员的姓名字符串，按字母顺序排序
    System.out.println(getTradersName(transactions));

    // 5.有没有交易员是在米兰工作的
    System.out.println(hasMilanTrader(transactions));

    // 6.返回交易员是剑桥的所有交易的交易额
    System.out.println(getCambridgeTransactionsValue(transactions));

    // 7.所有交易中，最高的交易额是多少
    System.out.println(getMaxTransactionValue(transactions));

    // 8.返回交易额最小的交易
    System.out.println(getMinTransaction(transactions));
  }

  public static List<Transaction> get2011Transactions(List<Transaction> transactions) {
    Stream<Transaction> stream = transactions.stream();
    stream=stream.filter((Transaction tr)-> tr.getYear()==2011);
    stream=stream.sorted(Comparator.comparingInt(Transaction::getValue));
    return stream.collect(Collectors.toList());
  }

  public static List<String> getTradersCity(List<Transaction> transactions) {
    Stream<Transaction> stream = transactions.stream();
    Stream<Trader> stream2 = stream.map((Transaction::getTrader));
    Stream<String> stream3 = stream2.map((Trader::getCity));
    stream3 = stream3.distinct();
    return stream3.collect(Collectors.toList());
  }

  public static List<Trader> getCambridgeTraders(List<Transaction> transactions) {
    Stream<Transaction> stream = transactions.stream();
    stream=stream.filter((Transaction tr)-> tr.getTrader().getCity().equals("Cambridge"));
    Stream<Trader>stream2 =stream.map(Transaction::getTrader);
    stream2=stream2.filter(distinctByKey(Trader::getName));
    stream2 = stream2.sorted(Comparator.comparing(Trader::getName));
    return stream2.collect(Collectors.toList());
  }

  public static List<String> getTradersName(List<Transaction> transactions) {
    Stream<Transaction> stream = transactions.stream();
    Stream<Trader> stream2 = stream.map(Transaction::getTrader);
    stream2 = stream2.filter(distinctByKey(Trader::getName));
    Stream<String> stream3 = stream2.map(Trader::getName);
    stream3=stream3.sorted();
    return stream3.collect(Collectors.toList());
  }

  public static boolean hasMilanTrader(List<Transaction> transactions) {
    Stream<Transaction> stream = transactions.stream();
    Stream<Trader> stream2 = stream.map(Transaction::getTrader);
    stream2=stream2.filter((Trader tr)-> tr.getCity().equals("Milan"));
    return !stream2.collect(Collectors.toList()).isEmpty();
  }

  public static List<Integer> getCambridgeTransactionsValue(List<Transaction> transactions) {
    Stream<Transaction> stream = transactions.stream();
    stream = stream.filter((Transaction tr)-> tr.getTrader().getCity().equals("Cambridge"));
    Stream<Integer> stream2=stream.map(Transaction::getValue);
    return stream2.collect(Collectors.toList());
  }

  public static int getMaxTransactionValue(List<Transaction> transactions) {
    Stream<Transaction> stream = transactions.stream();
    Stream<Integer> stream2=stream.map(Transaction::getValue);
    stream2=stream2.sorted(Collections.reverseOrder());
    stream2 = stream2.limit(1);
    AtomicInteger count = new AtomicInteger();
    stream2.forEach(count::set);
    return count.get();
  }

  public static Transaction getMinTransaction(List<Transaction> transactions) {
    Stream<Transaction> stream = transactions.stream();
    stream=stream.sorted(Comparator.comparing(Transaction::getValue));
    stream = stream.limit(1);
    List<Transaction> list = stream.collect(Collectors.toList());
    return list.get(0);
  }
  public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    Set<Object> seen = ConcurrentHashMap.newKeySet();
    return t -> seen.add(keyExtractor.apply(t));
  }
}
