package impl

/*
x <= 0 : ((((((tan(x) / csc(x)) + csc(x)) + ((sec(x) ^ 3) / sec(x))) + ((csc(x) * tan(x)) / tan(x))) / ((csc(x) + tan(x)) ^ 2)) ^ 2)
x > 0 : (((((log_10(x) / log_3(x)) / (log_3(x) * log_2(x))) * log_5(x)) + log_10(x)) + ((log_5(x) + ln(x)) * (log_5(x) ^ 3)))


tan csc sec
log_10 log_3 log_2 log_5
 */
class MainFun