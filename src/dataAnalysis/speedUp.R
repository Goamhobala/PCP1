library(tidyverse)
library(scales)
mac <- read.csv("analyses/mac2.csv")

summary(mac)
box_mac <- ggplot(mac,  aes(x=factor(size), y=speedup)) +
  geom_boxplot(color="blue", fill="orange", size=0.32)+
  labs(title="Box Plot of Speedup by Size\n on 8-core Macbook", x="Size", y="Speedup")+
  theme_minimal()+
  theme(plot.title = element_text(size=10, hjust=0.5))

connected_mac <- ggplot(mac,  aes(x=step, y=speedup)) +
  geom_point(color="coral", size=1.8)+
  labs(title="Scattered Plot of Speedup by Steps\n on 8-core Macbook", x="Steps", y="Speedup")+
  theme_minimal()+
  scale_x_continuous(breaks=seq(2000, 280000, 20000),
                     labels = label_number(scale = 1e-3, suffix = "k"))+ 
  theme(plot.title = element_text(size=10, hjust=0.5))
# Mean speed up
mean_speedup <- mac %>%
  group_by(size) %>%
  summarise(mean_speedup = mean(speedup))

mean_mac <- ggplot(mean_speedup, aes(x=factor(size), y=mean_speedup))+
  labs(title="Connected Scattered Plot of Mean Speedup\n by Size on 8-core Macbook", x="Size", y="Speedup")+
  geom_line(aes(x=factor(size), y=mean_speedup), group=1, color="blue", linetype="solid", size=1.5)+
  theme_minimal()+
  geom_point(aes(x=factor(size), y=mean_speedup), color="coral", size=3
             )+
  scale_y_continuous(breaks=seq(0.4, 2.4, 0.2))+
  theme(plot.title = element_text(size=10, hjust=0.5))

# windows
win <- read.csv("analyses/win.csv")
box_win <- ggplot(win,  aes(x=factor(size), y=speedup)) +
  geom_boxplot(color="green", fill="hotpink", size=0.32)+
  labs(title="Box Plot of Speedup by Size\n on 4-core Surface Laptop 2", x="Size", y="Speedup")+
  theme_minimal()+
  theme(plot.title = element_text(size=10, hjust=0.5))

connected_win <- ggplot(win,  aes(x=steps, y=speedup)) +
  geom_point(color="hotpink", size=1.8)+
  labs(title="Scattered Plot of Speedup by Steps\n on 4-core Surface Laptop 2", x="Steps", y="Speedup")+
  theme_minimal()+
  scale_x_continuous(breaks=seq(2000, 280000, 20000),
                     labels = label_number(scale = 1e-3, suffix = "k"))+ 
  theme(plot.title = element_text(size=10, hjust=0.5))
# Mean speed up
mean_speedup <- win %>%
  group_by(size) %>%
  summarise(mean_speedup = mean(speedup))

mean_win <- ggplot(mean_speedup, aes(x=factor(size), y=mean_speedup))+
  labs(title="Connected Scattered Plot of Mean Speedup\n by Size on 4-core Surface Laptop 2", x="Size", y="Speedup")+
  geom_line(aes(x=factor(size), y=mean_speedup), group=1, color="palegreen", linetype="solid", size=1.5)+
  theme_minimal()+
  geom_point(aes(x=factor(size), y=mean_speedup), color="hotpink", size=3
  )+
  scale_y_continuous(breaks=seq(0.4, 2.4, 0.2))+
  theme(plot.title = element_text(size=10, hjust=0.5))



grid.arrange(box_mac, connected_mac, mean_mac, box_win, connected_win, mean_win, ncol=3, nrow=2)

