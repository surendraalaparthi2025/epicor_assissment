package com.assessment.epicor.service;

import com.assessment.epicor.model.Response;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class EpicorService {

    private static final Logger logger = LoggerFactory.getLogger(EpicorService.class);
    private static final CharArraySet REMOVE_WORDS = EnglishAnalyzer.getDefaultStopSet();
    private static final Pattern CLEAN_PATTERN = Pattern.compile("[^a-zA-Z\\s']");


    private static final Set<String> proNounsSet = Set.of(
            "he", "she", "it", "its", "they", "them", "his", "her", "their", "all",
            "we", "us", "you", "i", "me", "my", "mine", "our", "ours", "your", "yours", "him", "hers", "one", "which", "some", "what"
    );

    private static final Set<String> conjunctionSet = Set.of(
            "so", "yet", "although", "because", "since", "unless", "until", "while",
            "after", "before", "once", "though", "when", "whenever", "where",
            "wherever", "whereas", "than", "whether", "either", "neither", "both"
    );

    private static final Set<String> prePositionSet = Set.of(
            "from", "about", "above", "across", "against", "along", "among", "around", "before",
            "behind", "below", "beneath", "beside", "between", "beyond", "during", "inside",
            "near", "off", "out", "over", "through", "under", "until", "upon", "within", "without", "like", "up"
    );

    private static final Set<String> verbsSet = Set.of(
            "is", "am", "are", "was", "were", "be", "being", "been",

            // Forms of "have"
            "have", "has", "had",

            // Forms of "do"
            "do", "does", "did",

            // Modal verbs
            "can", "could", "may", "might", "must",
            "shall", "should", "will", "would"
    );




    public Response fileRead(String furl) throws Exception {
        if (furl == null || furl.isBlank()) {
            furl = "https://courses.cs.washington.edu/courses/cse390c/22sp/lectures/moby.txt";
        }
        logger.info("Fetching text from URL: {}", furl);

        List<String> countList = getWords(furl);
        logger.info("countList without Filters: {}", countList.size());

        logger.info("Going to find top 5 words{}...");

        Map<String, Long> countMap = countList.stream().map(w -> w.startsWith("'") ? w.substring(1): w)
                .filter(w -> !w.isBlank() && !REMOVE_WORDS.contains(w) &&  !proNounsSet.contains(w)
        && !conjunctionSet.contains(w) && !prePositionSet.contains(w) && !verbsSet.contains(w))
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));


        /*System.out.println("is: "+countMap.get("is"));
        System.out.println("was: "+countMap.get("was"));
        System.out.println("he: "+countMap.get("he"));
        System.out.println("she: "+countMap.get("she"));
        System.out.println("his: "+countMap.get("his"));
        System.out.println("our: "+countMap.get("our"));*/

       /* System.out.println("so: "+countMap.get("so"));
        System.out.println("from: "+countMap.get("from"));*/

       /* System.out.println("all: "+countMap.get("all"));

        System.out.println("had: "+countMap.get("had"));
        System.out.println("have: "+countMap.get("have"));
        System.out.println("were: "+countMap.get("were"));*/


       /* System.out.println("like: "+countMap.get("like"));
        System.out.println("which: "+countMap.get("which"));
        System.out.println("some: "+countMap.get("some"));*/

       /* System.out.println("now: "+countMap.get("now"));
        System.out.println("up: "+countMap.get("up"));*/


        Map<String, Long> top5WordsMap = countMap.entrySet().stream().
                sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())).limit(5).collect(Collectors.toMap(
                Map.Entry:: getKey, Map.Entry :: getValue, (w1, w2) -> w1, LinkedHashMap::new
        ));

        logger.info("top 5 words {} ..."+ top5WordsMap);


        Set<String> top50Set =  countMap.keySet();
        System.out.println(top50Set.size());

        /*Set<String> top50Words = countMap.keySet().stream().distinct().sorted().limit(50).collect(Collectors.toSet());
        System.out.println("top50Words:"+top50Words);*/

        TreeSet<String> top50WordsSet = countMap.keySet().stream().distinct().sorted().limit(50).collect(Collectors.toCollection(TreeSet::new));
        System.out.println("top50WordsSet:"+top50WordsSet);

        logger.info("top 50 unique records after Excluding conditions {} "+top50WordsSet);

        Response response =  new Response();
        response.setCount(countList.size());
        response.setTop5Words(top5WordsMap);
        response.setTop50Words(top50WordsSet);
        return response;
    }

    private List<String> getWords(String furl) throws Exception {
        List<String> wordList = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(furl).openStream()))) {
            bufferedReader.lines().map(l -> l.toLowerCase(Locale.ROOT)).map(l -> l.replace("'", "")).
                    map(l -> CLEAN_PATTERN.matcher(l).replaceAll(" ")).flatMap(l -> Arrays.stream(l.split("\\s+"))).
                    map(word -> word.endsWith("'s") ? word.substring(0, word.length() - 2) : word).
                    filter(word -> !word.isBlank()).forEach(wordList::add);
        }
        return wordList;
    }
}

