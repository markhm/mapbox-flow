package com.github.markhm.mapbox.views.dk;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DataLoader {
    private static Log log = LogFactory.getLog(DataLoader.class);

    public static final String DATA_FILE = "personal/kombit/2019_11.csv";

    private String dataFile = null;

    private List<KommuneData> data = null;

    public DataLoader(String dataFile) {
        this.dataFile = dataFile;
    }

    public static void main(String[] args) throws Exception {
        DataLoader dataLoader = new DataLoader(DATA_FILE);
        dataLoader.load();

        dataLoader.data.forEach(element -> System.out.println(element));
    }

    public List<KommuneData> getData() {
        return data;
    }

    public void load() {
        try {
            data = new ArrayList<>();

            URL url = this.getClass().getClassLoader().getResource(dataFile);

            BufferedReader reader = new BufferedReader(new FileReader(url.getFile()));
            String line = reader.readLine();
            while (line != null && !line.startsWith("#")) {
                StringTokenizer tokenizer = new StringTokenizer(line, ",");
                if (tokenizer.hasMoreTokens()) {
                    String kommune = tokenizer.nextToken();
                    if (!kommune.contains("kommune") || kommune.startsWith("#")) {
                        int calls = Integer.parseInt(tokenizer.nextToken());

                        KommuneData kommuneData = new KommuneData(kommune, calls);
                        data.add(kommuneData);
                    }
                } else {
                    log.info("Could not find more/any tokens on line: " + line);
                }

                line = reader.readLine();
            }

            binData(data);
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void binData(List<KommuneData> data) {
        data.forEach(e -> {

            long numberOfCalls = e.getCalls();
            if (numberOfCalls > 5000000) {
                e.setBin(7);
            } else if (numberOfCalls > 2000000) {
                e.setBin(6);
            } else if (numberOfCalls > 1000000) {
                e.setBin(5);
            } else if (numberOfCalls > 500000) {
                e.setBin(4);
            } else if (numberOfCalls > 200000) {
                e.setBin(3);
            } else if (numberOfCalls > 20000) {
                e.setBin(2);
            } else if (numberOfCalls > 2000) {
                e.setBin(1);
            } else {
                e.setBin(0);
            }
        });
    }

    public class KommuneData {
        String kommune;
        int calls;
        int bin;

        public KommuneData(String kommune, int calls) {
            this.kommune = kommune;
            this.calls = calls;
        }

        public KommuneData(String kommune, int calls, int bin) {
            this(kommune, calls);
            this.bin = bin;
        }

        public long getCalls() {
            return calls;
        }

        public void setBin(int bin) {
            this.bin = bin;
        }

        public int getBin() {
            return bin;
        }

        public String getKommune() {
            return kommune;
        }

        @Override
        public String toString() {
            return "KommuneData{" +
                    "kommune='" + kommune + '\'' +
                    ", calls=" + calls +
                    ", bin=" + bin +
                    '}';
        }
    }

}
