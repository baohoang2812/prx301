<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : courseraStyleSheet.xsl
    Created on : June 29, 2020, 9:16 PM
    Author     : Gia Bảo Hoàng
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:output method="xml" encoding="UTF-8"/>

    <xsl:template match="/">
        <course>
            <name>
                <xsl:value-of select="//h1[contains(@class,'banner-title')]" />
            </name>
            <url>
                <!--<xsl:value-of select="" />-->
            </url>
            <!--Only Specilization Has--> 
            <summary>
                <!--<xsl:value-of select="" />--> 
            </summary>
            <description>
                <xsl:value-of select="//*[contains(@class, 'cdp-about')]//*[@class='content-inner']/p" />
            </description>
            <providers>
                <xsl:for-each select="//*[contains(@class, 'PartnerList')]/div[2]/div">
                    <provider>
                        <providerName>
                            <xsl:value-of select=".//h4[contains(@class,'rc-Partner__title')]" />
                        </providerName>
                        <providerLogo>
                            <!--<xsl:value-of select="" />-->
                        </providerLogo>
                    </provider>
                </xsl:for-each>
            </providers>
            <ratingPoint>
                <xsl:value-of select="substring-before(//*[contains(@class,'XDPRating')]//*[contains(@class,'rating-text')][1],'s')" />
            </ratingPoint>
            <ratingNumber>
                <xsl:value-of select="substring-before(//*[contains(@class,'XDPRating')]//*[contains(@class,'ratings-count-expertise-style')]/span,' ')" />
            </ratingNumber>
            <enrollmentNumber>
                <xsl:value-of select="//*[contains(@class,'rc-ProductMetrics')]//strong/span" />
            </enrollmentNumber>
            <skillLevel>
                <xsl:value-of select="substring-before(//*[contains(@class, 'ProductGlance')]/div[4]/div[2]/div, ' ')" />
            </skillLevel>
            <author>
                <xsl:value-of select="//*[contains(@class, 'banner-instructor-info')]//span[1]" />
            </author>
            <source>
                Coursera
            </source>
        </course>
    </xsl:template>
</xsl:stylesheet>
