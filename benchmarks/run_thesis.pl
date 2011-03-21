#!/usr/bin/perl

sub benchmark_average {
  $max=10;
  $sum_scala=0;
  $sum_rop=0;
  for($i=0; $i<$max; $i++) {
    @times = split(/:/, `scala -cp ../bin/:../../Roles/bin applications.Benchmarks 1 $_[0]`);
    print "run $i had slowdown $times[0] (Scala roles) and $times[1] (ROP)\n";
    $sum_scala += $times[0];
    $sum_rop += $times[1];
  }
  $av_scala = $sum_scala / $max;
  $av_rop = $sum_rop / $max;
  print "average: $av_scala (Scala roles) and $av_rop (ROP)\n";
}

benchmark_average thesis
