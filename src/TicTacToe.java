package com.tom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
    static ArrayList<Integer> graczPozycje = new ArrayList<>();
    static ArrayList<Integer> cpuPozycje = new ArrayList<>();
    static ArrayList<Integer> possMoves = new ArrayList<>();
    static List<Integer> r1 = Arrays.asList(1, 2, 3);
    static List<Integer> r2 = Arrays.asList(4, 5, 6);
    static List<Integer> r3 = Arrays.asList(7, 8, 9);
    static List<Integer> c1 = Arrays.asList(3, 6, 9);
    static List<Integer> c2 = Arrays.asList(2, 5, 8);
    static List<Integer> c3 = Arrays.asList(1, 4, 7);
    static List<Integer> s1 = Arrays.asList(1, 5, 9);
    static List<Integer> s2 = Arrays.asList(3, 5, 7);
    static List<List> warunkiWygr = new ArrayList<>();
    //    static int[] punkt = new int[2];
//    static int r = 0;//0,1,2 rzšd
//    static int k = 1;//0,1,2 kolumna
    static char[][] plansza = {
            {' ', '|', ' ', '|', ' '},
            {'-', '+', '-', '+', '-'},
            {' ', '|', ' ', '|', ' '},
            {'-', '+', '-', '+', '-'},
            {' ', '|', ' ', '|', ' '}};
    static boolean c = false;
    static boolean koniec = false;
    static boolean Koniec = false;

    // zamiana r i k na pozycję - funkcje testowe ver 13
//    public static Integer Punkt(int r, int k) {
//
//        return (k + 1) + 3 * r;
//    }
//
//    private static Integer R(int pos) {
//
//        int R = switch (pos) {
//            case 1, 2, 3:
//                yield 0;
//            case 4, 5, 6:
//                yield 2;
//            case 7, 8, 9:
//                yield 4;
//            default:
//                yield -1;
//        };
//        return R;
//    }
//
//    private static Integer K(int pos) {
//
//        int K = switch (pos) {
//            case 1, 4, 7:
//                yield 0;
//            case 2, 5, 8:
//                yield 2;
//            case 3, 6, 9:
//                yield 4;
//            default:
//                yield -1;
//        };
//        return K;
//    }

    public static void main(String[] args) {
        //System.out.println("Test metody Punkt. Obliczona poz. "+Punkt(r,k)+" dla r="+r+" k="+k);// działa
        System.out.println("Tic Tac Toe - SMART");


        while (!Koniec) {//pętla całej gry
            System.out.println("Gramy?");
            System.out.println("1 - tak, ja zaczynam");
            System.out.println("2 - tak, cpu zaczyna");
            System.out.println("3 - nie, mam dosyć");
            //czyszczenie planszy przed nowš grš
            for (int i = 1; i < 10; i++) {
                podajPoz(plansza, i, "new");
            }
            cpuPozycje.clear();
            graczPozycje.clear();
            possMoves.clear();

            setFreeMoves();
            setWygrane();

            c = false;
            //System.out.println();

            Scanner scan = new Scanner(System.in);//nowy skan
            int wyb = scan.nextInt();
            switch (wyb) {
                case 1:
                    Koniec = false;
                    koniec = false;
                    drukPlanszy(plansza);
                    //System.out.println("Do następnego razu");
                    break;
                case 2: {
                    Koniec = false;
                    koniec = false;
                    podajPoz(plansza, 5, "CPU");
                    break;
                }
                case 3: {
                    Koniec = true;
                    koniec = true;
                    System.out.println("Do następnego razu :-)");
                    break;
                }
                default:
                    break;
            }


            while (!koniec) { //mała pętla - akcje po każdym ruchu
                System.out.println("Wolne pola: " + possMoves);//pokaż wolne pola - Lista
                if (possMoves.size() == 1) {
                    podajPoz(plansza, possMoves.get(0), "gracz");
                    System.out.println("Wstawiłem 'X' za ciebie - nie było wyboru");
                } else {
                    scan = new Scanner(System.in);//nowy skan
                    int poz = scan.nextInt();//odczyt pozycji gracza
                    while (!possMoves.contains(poz)) {//czy pole dostępne
                        System.out.print("Pole zajęte, podaj wolne: ");
                        poz = scan.nextInt();

                    }
                    podajPoz(plansza, poz, "gracz");//umieć pole gracza w tablicy i przerysuj
                }

                if (cpuPozycje.size() > 1 || graczPozycje.size() > 1) {
                    checkRisk();//analiza pozycji, kto już zajšł min. dwa pola
                }
                if (!c) {
                    smartPos();//losowanie jeżeli nie ma ryzyka ani wygranej
                }
                if (possMoves.size() == 0) {//do rozważenia - czy można wygrać w ostatnim ruchu?
                    System.out.println("Mamy remis!!!");
                    koniec = true;
                }
                //System.out.println();
                String wynik = sprWygr();
                if (wynik.length() > 0) {
                    System.out.println(wynik);
                    koniec = true;
                    break;
                }
            }

        }
    }


    public static void setFreeMoves() {
        for (int i = 1; i < 10; i++)
            possMoves.add(i);
    }

    public static void checkRisk() {
        //sprawdzenie szansy wygrania
        if (cpuPozycje.size() > 1 && !koniec) {
            for (int i = 0; i < possMoves.size(); i++) {
                cpuPozycje.add(possMoves.get(i));//temp add
                for (List m : warunkiWygr)
                    if (cpuPozycje.containsAll(m)) {
                        cpuPozycje.remove(cpuPozycje.size() - 1);
                        System.out.println("CPU wygrywa, ostatnie pole " + possMoves.get(i));
                        podajPoz(plansza, possMoves.get(i), "CPU");//get i
                        c = true;//nie losuj, pole wybrane, koniec gry
                        koniec = true;
                        break;
                    } else {
                        c = false;
                        koniec = false;
                    }
                if (!c) {
                    cpuPozycje.remove(cpuPozycje.size() - 1);
                }
                if (koniec) break;
            }
        }
        System.out.println();
        // sprawdzenie ryzyka przegranej i blokowanie
        if (graczPozycje.size() > 1 && !koniec) {
            for (int i = 0; i < possMoves.size(); i++) {
                graczPozycje.add(possMoves.get(i));
                for (List k : warunkiWygr)
                    if (graczPozycje.containsAll(k)) {
                        graczPozycje.remove(graczPozycje.size() - 1);
                        System.out.println("Gracz może wygrać, blokuję pole " + possMoves.get(i));
                        podajPoz(plansza, possMoves.get(i), "CPU");//blokowanie gracza
                        System.out.println();
                        c = true;//pole wybrane - oddaj ruch
                        break;
                    }

                if (c) {
                    break;
                }else graczPozycje.remove(graczPozycje.size() - 1);
            }
        }
        if (!c) {
            smartPos();
        }
    }

    public static void drukPlanszy(char[][] plansza) {

        for (char[] wiersz : plansza) {
            for (char z : wiersz) {
                System.out.print(z);
            }
            System.out.println();
        }
    }

    public static void podajPoz(char[][] plansza, int poz, String kto) {
        char symbol = ' ';
        switch(kto){
            case "gracz":
                symbol = 'X';
                graczPozycje.add(poz);
                break;
            case "CPU":
                symbol = 'O';
                cpuPozycje.add(poz);
                break;
            default:
                break;
        }


        possMoves.remove(Integer.valueOf(poz));
        // sprawdzenie
        //System.out.print("r=" + R(poz) + "  ");
        //System.out.println("k=" + K(poz));
        switch (poz) {
            case 1:
                plansza[0][0] = symbol;
                break;
            case 2:
                plansza[0][2] = symbol;
                break;
            case 3:
                plansza[0][4] = symbol;
                break;
            case 4:
                plansza[2][0] = symbol;
                break;
            case 5:
                plansza[2][2] = symbol;
                break;
            case 6:
                plansza[2][4] = symbol;
                break;
            case 7:
                plansza[4][0] = symbol;
                break;
            case 8:
                plansza[4][2] = symbol;
                break;
            case 9:
                plansza[4][4] = symbol;
                break;
            default:
                break;
        }
        if (symbol != ' ') {
            drukPlanszy(plansza);
            System.out.println();
        }
    }

    //Sprytne wybieranie pozycji dla CPU
    public static void smartPos() {
        if (possMoves.size() > 0) {
            if (plansza[2][2] != ' ') {
                Random rand = new Random();
                int kompPozInd = rand.nextInt(possMoves.size());
                int kompPoz = possMoves.get(kompPozInd);
                System.out.println("Nie ma zagrożenia - losuję: " + kompPoz);
                podajPoz(plansza, kompPoz, "CPU");
            } else {
                System.out.println("No to oczywiście: " + 5);
                podajPoz(plansza, 5, "CPU");
            }
        }
        c = true;//reset, nie można losować
    }

    public static void setWygrane() {
        warunkiWygr.add(r1);
        warunkiWygr.add(r2);
        warunkiWygr.add(r3);
        warunkiWygr.add(c1);
        warunkiWygr.add(c2);
        warunkiWygr.add(c3);
        warunkiWygr.add(s1);
        warunkiWygr.add(s2);
    }

    public static String sprWygr() {//Lista wygrywajšcych układów

        for (List<?> l : warunkiWygr) {
            if (cpuPozycje.size() > 2 && cpuPozycje.containsAll(l)) {
                return "Przegrałeś!!!";
            } else if (graczPozycje.size() > 2 && graczPozycje.containsAll(l)) {
                return "Wygrałeś!!!";
            } else if (graczPozycje.size() + cpuPozycje.size() == 9) {
                return "Remis";
            }
        }
        return "";
    }
}
