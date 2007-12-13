#!/usr/bin/perl

use strict;
use RPC::XML;
use RPC::XML::Client;
use IO::Socket::Multicast;

my $s = IO::Socket::Multicast->new(LocalPort=>6789);
$s->mcast_add('224.0.0.1');
$s->mcast_send('jameica.xmlrpc.queue','224.0.0.1:6789');

my $url;
do
{
  $s->recv($url,1024);
}
while ($url eq "jameica.xmlrpc.queue"); # Das ist das Echo von uns selbst
$s->mcast_drop('224.0.0.1');

my $cli = RPC::XML::Client->new($url);

################################################################################
# Test 1:
# Nachricht senden

# Message erzeugen
my @msg;
push(@msg,RPC::XML::string->new("channel"));   # Name des Channels
push(@msg,RPC::XML::string->new("recipient")); # Name des Empfaengers
push(@msg,RPC::XML::base64->new("Das ist die eigentliche Nachricht")); # Inhalt der Nachricht
 
# Message an Queue senden
my $resp = $cli->send_request("jameica.xmlrpc.queue.put",@msg);
##
################################################################################

################################################################################
# Test 2:
# Nachricht abrufen

# Message erzeugen
my @msg;
push(@msg,RPC::XML::string->new("channel"));   # Name des Channels
push(@msg,RPC::XML::string->new("recipient")); # Name des Empfaengers

# Message an Queue senden
my $resp = $cli->send_request("jameica.xmlrpc.queue.get",@msg);
print "Empfangene Nachricht: ".$resp->value."\n";
