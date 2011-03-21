#!/usr/bin/perl

sub benchmark_average {
  $max=100;
  $i=0;
  while($i<$max) {
    $time = `scala -cp ../bin/:../../Roles/bin applications.SingleCall $max`;
    print "$i $time\n";
    if ($i < 20) { $i++; }
    else { $i += 10; }
  }
}

benchmark_average
